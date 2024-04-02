`user_replenishment` example:
```bash
POST /user_replenishment HTTP/1.1
Accept: application/json, */*;q=0.5
Accept-Encoding: gzip, deflate
Connection: keep-alive
Content-Length: 79
Content-Type: application/json
Host: localhost:1234
User-Agent: HTTPie/3.2.1
X-Request-Id: 42

{
    "reward": "15",
    "ticketId": "12345",
    "userName": {
        "gitlab_name": "catdog905"
    }
}


HTTP/1.1 200 OK
Connection: keep-alive
Content-Length: 0
Date: Tue, 02 Apr 2024 18:39:24 GMT
```