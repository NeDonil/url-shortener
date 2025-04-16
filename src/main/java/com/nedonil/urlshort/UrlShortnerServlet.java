package com.nedonil.urlshort;

import com.nedonil.urlshort.db.Dao;
import com.nedonil.urlshort.db.UrlDao;
import com.nedonil.urlshort.model.Url;
import com.nedonil.urlshort.utils.Utils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class UrlShortnerServlet extends HttpServlet {

    private final Dao<Url> taskDao = new UrlDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var newPath = req.getParameter("path");
        sendBadRequestIfEmpty(resp, newPath, "path is required");

        var base62Path = Utils.encodeStringToBase62(newPath);
        Url url = new Url(UUID.randomUUID().toString(), newPath, base62Path);
        taskDao.save(url);
        resp.getWriter().println(base62Path);
        resp.getWriter().close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Key is required in path");
            return;
        }

        String key = Utils.extractKeyFromPath(pathInfo);
        Url url = taskDao.findByKey(key);

        if (url == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "URL not found");
            return;
        }

        resp.sendRedirect(url.getPath());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Key is required in path");
            return;
        }

        String key = Utils.extractKeyFromPath(pathInfo);
        sendBadRequestIfEmpty(resp, key, "key is required");
        taskDao.deleteByKey(key);
    }

    private void sendBadRequestIfEmpty(HttpServletResponse resp, String value, String message) throws IOException {
        if (Objects.isNull(value)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
        }
    }
}
