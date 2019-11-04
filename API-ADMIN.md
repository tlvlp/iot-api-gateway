# IoT Server API Gateway - Admin API
 
The following endpoints are for administrative purposes and most of them are accessible with the ADMIN role 
save for the authentication that is accessible by any authenticated user.
Actual API endpoints are inherited from the project's [deployment repository](https://github.com/tlvlp/iot-server-deployment) via environment variables.

## Authenticate User:
| | |
| :--- | :--- |
| URL | ${API_GATEWAY_API_AUTHENTICATE_USER} |
| Method | GET |
| Content-Type | application/json |
| URL Parameters | userID: String - The User's login ID |
| | password: String - The User's password |
| Request Body | None |
| Success Response | Code: 200 OK  |
| | Contents: None |
| Error Response | Code: 404 NOT FOUND |
| Authorization | Any authenticated user! |

Sample request:
```bash
curl -X POST \
  -u username:password \
  https://0.0.0.0:8544/users/auth
```
Sample output:
```json
{
    "userID": "test_user",
    "firstName": "Test",
    "lastName": "User",
    "password": "",
    "email": "test.user@gmail.com",
    "roles": [
        "USER"
    ],
    "active": true
}
```

## Get All Users:
| | |
| :--- | :--- |
| URL | ${API_GATEWAY_API_GET_ALL_USERS} |
| Method | GET |
| Content-Type | application/json |
| URL Parameters | None |
| Request Body | None |
| Success Response | Code: 202 ACCEPTED  |
| | Contents: List of the requested Users (see below) |
| Error Response | No specific error |
| Authorization | ADMIN |

Sample Request:
```bash
curl -X GET \
  -u username:password \
  https://0.0.0.0:8544/users/admin/all \
  -H 'Content-Type: application/json'
```

Sample output:
```json
[
    {
        "userID": "backend",
        "firstName": "TECHNICAL",
        "lastName": "ACCOUNT",
        "password": "",
        "email": "none@none.com",
        "roles": [
            "BACKEND"
        ],
        "active": true
    },
    {
        "userID": "user",
        "firstName": "TECHNICAL",
        "lastName": "ACCOUNT",
        "password": "",
        "email": "none@none.com",
        "roles": [
            "USER"
        ],
        "active": true
    },
    {
        "userID": "admin",
        "firstName": "TECHNICAL",
        "lastName": "ACCOUNT",
        "password": "",
        "email": "none@none.com",
        "roles": [
            "ADMIN"
        ],
        "active": true
    }
]

```

## Save user:
| | |
| :--- | :--- |
| URL | ${API_GATEWAY_API_SAVE_USER} |
| Method | POST |
| Content-Type | application/json |
| URL Parameters | None |
| Request Body | User object (see below) |
| Success Response | Code: 202 ACCEPTED  |
| | Contents: None |
| Error Response | No specific error |
| Authorization | ADMIN |

Sample request:
```bash
curl -X POST \
  -u username:password \
  https://0.0.0.0:8544/users/admin/save \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: a7e23ecf-06d3-4330-8c87-68c482a9cbea' \
  -H 'cache-control: no-cache' \
  -d '{
    "userID": "testuser",
    "firstName": "Test",
    "lastName": "User",
    "password": "testpass",
    "email": "none@none.com",
    "roles": [
        "USER"
    ],
    "active": true
}'
```

## Delete User:
| | |
| :--- | :--- |
| URL | ${API_GATEWAY_API_DELETE_USER} |
| Method | POST |
| Content-Type | application/json |
| URL Parameters | None |
| Request Body | User object but only the userID is required (see below) |
| Success Response | Code: 200 OK  |
| | Contents: None |
| Error Response | No specific error |
| Authorization | ADMIN |

Sample request:
```bash
curl -X POST \
  -u username:password \
  https://0.0.0.0:8544/users/admin/delete \
  -H 'Content-Type: application/json' \
  -d '{
    "userID": "testuser"
}'
```

## Get Roles:
| | |
| :--- | :--- |
| URL | ${API_GATEWAY_API_GET_ROLES} |
| Method | GET |
| Content-Type | application/json |
| URL Parameters | None |
| Request Body | None |
| Success Response | Code: 200 OK  |
| | Contents: A list of available roles |
| Error Response | Code: 404 NOT FOUND |
| Authorization | ADMIN |

Sample request:
```bash
curl -X GET \
  -u username:password \
  https://0.0.0.0:8544/users/admin/roles \
  -H 'Content-Type: application/json' 
```

Sample output:
```json
[
    "ADMIN",
    "USER",
    "BACKEND"
]
```