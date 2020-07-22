# Netflix OSS
A Microservice stack example using Netflix OSS

This code is based on the Book's implementation Spring MicroServices in Action (https://github.com/carnellj/spmia-chapter7). The version of the spring boot was upgraded to 2.X.X and some other adjustments e.g. Redis to Oauth2 tokens.

![Architecture](https://github.com/yvesmendes/netflixoss/blob/master/resource/architecture.png?raw=true)

## Pre-Requisities ##
- maven
- JDK 1.8
- Docker 

# Run

Just run: 
```
1) mvn package dockerfile:build
2) docker-compose -f docker/common/docker-compose.yml up
3) Get Token

curl -X POST \
  'http://localhost:8080/oauth/token?grant_type=password&username=user&password=pass' \
  -H 'Authorization: Basic Y2xpZW50SWQ6c2VjcmV0' \
  -H 'Postman-Token: 442aad7d-494d-4b22-9dd5-dd88fb736a81' \
  -H 'cache-control: no-cache'

4) Get License

curl -X GET \
  http://localhost:5555/api/licensing/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a \
  -H 'Authorization: bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJvcmdhbml6YXRpb25JZCI6ImQxODU5ZjFmLTRiZDctNDU5My04NjU0LWVhNmQ5YTZhNjI2ZSIsImV4cCI6MTU5NTEwNjUxNiwidXNlcl9uYW1lIjoidXNlciIsImp0aSI6IjNmN2E3ZGVmLTViODQtNDI2My04MjUxLWE4ZmY3OTNlMWY2ZiIsImNsaWVudF9pZCI6ImNsaWVudElkIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl19.RLU9lYmoOVqBV08_DZwL7CGwqv_olszRdBkto7bnJeZBKC-_HoHgm-dzRxtmdWfONCn5_v0f_xuXfawHc4DYDL6gDl_DZhPmsXLvgMpiYyZcKq8sdXYt1q343xMqo0aqgq6-k_EkAKA0gQcdEFcPqkdcSQq9hv4aXcRjiC8SzxYsFKjGEByrw-ZEgGcGCbmCRLjW9kF84csPlNcn86JQqYJDE9qT9N04dFz-1S6-AX3mxhAK81VlqrK-Gf-nPSpzphg2rWXxYQ5_ztuB8Y8d8MiCTgB9cAlsEWWXlHfGCRsFs3GG_B3JD8E4NC6yH1-30910EyTMMCfSLub0H9cRdw' \
  -H 'Postman-Token: 76855401-b012-4a31-bad9-3d5235a15a50' \
  -H 'cache-control: no-cache'
  
```
