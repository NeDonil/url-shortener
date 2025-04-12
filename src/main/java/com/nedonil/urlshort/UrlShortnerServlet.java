package com.nedonil.urlshort;

import com.nedonil.urlshort.db.Dao;
import com.nedonil.urlshort.db.UrlDao;
import com.nedonil.urlshort.model.Url;
import com.nedonil.urlshort.utils.EncodeUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class UrlShortnerServlet extends HttpServlet {

    private final Dao<Url> taskDao = new UrlDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var newPath = req.getParameter("path");
        var base62Path = EncodeUtils.encodeStringToBase62(newPath);
        Url url = new Url(UUID.randomUUID().toString(), newPath, base62Path);
        taskDao.save(url);
        resp.getWriter().println(base62Path);
        resp.getWriter().close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var key = req.getParameter("key");
        resp.sendRedirect(taskDao.findByKey(key).getPath());
    }

}
