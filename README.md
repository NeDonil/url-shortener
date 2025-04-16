# URL Shortener (Yandex Cloud Serverless)

A serverless URL shortener built for a coursework project at VSTU. Shortens long URLs into Base62-encoded keys and redirects users upon access.

## Features

* **Shorten URLs**: Submit a URL via POST request to generate a short Base62 key.
* **Redirect**: Access the short key via GET request to be redirected to the original URL.
* **Serverless**: Deployed on Yandex Cloud Functions
* **Persistence**: Uses a DAO pattern (e.g., Yandex Database (YDB) or any other supported `Dao<Url>` implementation).

## Technologies

* Java (Servlet API)
* Base62 encoding (custom or library-based)
* Yandex Cloud Serverless Functions
* Database: YDB (via `UrlDao`)

### YDB Table Creation for `Url` Entity

#### YQL Query

Execute this in **YDB Console** or via SDK to create the table:

```sql
CREATE TABLE urls (
    url_id Utf8 NOT NULL,
    path Utf8 NOT NULL,
    key Utf8 NOT NULL,
    PRIMARY KEY (url_id)
);
```

## Environment Variables

The `UrlDao` requires the following environment variables to connect to **Yandex Database (YDB)**:


| Variable    | Description                                                                                                        | Example Value                                  |
| ----------- | ------------------------------------------------------------------------------------------------------------------ | ---------------------------------------------- |
| `DATABASE`  | Full path to your YDB database (includes endpoint and database name).                                              | `/ru-central1/b1gxxxxxxxx/etn0xxxxxxxx`        |
| `ENDPOINT`  | YDB server endpoint (gRPC protocol).                                                                               | `grpcs://ydb.serverless.yandexcloud.net:2135`  |
| `IAM_TOKEN` | OAuth token for authentication (or[IAM token](https://cloud.yandex.ru/docs/iam/concepts/authorization/iam-token)). | `t1.9euelZq...` (or use `yc iam create-token`) |

## Yandex Cloud Function Deployment Guide

### Prerequisites

1. Install [Yandex Cloud CLI (yc)

```bash
curl https://storage.yandexcloud.net/yandexcloud-yc/install.sh | bash
```

2. Authenticate Yandex cloudyc init

```bash
yc init
```

### Deployment Steps

1. **Prepare the package**

   ```bash
   chmod +x pack.sh deploy.sh
   ./pack.sh  # Creates target.zip
   ```
2. **Configure environment**

   * Edit `deploy.sh` and set:
     * `DB_NAME` - Full YDB database path
     * `DB_ENDPOINT` - YDB endpoint address
     * `IAM_TOKEN` - Valid IAM token (use `yc iam create-token`)
3. **Deploy the function**

   ```
   ./deploy.sh
   ```

## Example

```
// POST /?path=https://very-long-url.com/example  
// Response: "abc123"  

// GET /?key=abc123  
// Redirects to: https://very-long-url.com/example  
```
