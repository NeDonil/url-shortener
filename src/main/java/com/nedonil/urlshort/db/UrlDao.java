package com.nedonil.urlshort.db;

import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.values.PrimitiveValue;
import com.nedonil.urlshort.model.Url;
import com.nedonil.urlshort.utils.ThrowingConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class UrlDao implements Dao<Url> {

    private final EntityManager entityManager = new EntityManager(
      System.getenv("DATABASE"),
      System.getenv("ENDPOINT"),
      System.getenv("IAM_TOKEN")
    );

    @Override
    public List<Url> findAll() {
        var urls = new ArrayList<Url>();
        entityManager.execute("select * from urls", Params.empty(), ThrowingConsumer.unchecked(result -> {
            var resultSet = result.getResultSet(0);
            while (resultSet.next()) {
                urls.add(Url.fromResultSet(resultSet));
            }
        }));
        return urls;
    }

    @Override
    public void save(Url url) {
        entityManager.execute(
                "declare $urlId as Utf8;" +
                        "declare $path as Utf8;" +
                        "declare $key as Utf8;" +
                        "insert into urls(url_id, path, key) values ($urlId, $path, $key)",
                Params.of("$urlId", PrimitiveValue.utf8(UUID.randomUUID().toString()),
                        "$path", PrimitiveValue.utf8(url.getPath()),
                        "$key", PrimitiveValue.utf8(url.getKey()))
        );
    }

    @Override
    public void deleteById(String urlId) {
        entityManager.execute(
                "declare $urlId as Utf8;" +
                        "delete from urls where url_id = $urlId",
                Params.of("$taskId", PrimitiveValue.utf8(urlId)));
    }

    @Override
    public Url findByKey(String key) {
        AtomicReference<Url> url = new AtomicReference<>();
        entityManager.execute("declare $key as Utf8; select * from urls where key = $key",
          Params.of("$key", PrimitiveValue.utf8(key)), ThrowingConsumer.unchecked(result -> {
            var resultSet = result.getResultSet(0);
            while (resultSet.next()) {
                url.set(Url.fromResultSet(resultSet));
            }
        }));
        return url.get();
    }

}