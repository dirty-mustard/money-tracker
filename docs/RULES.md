# Rules API

<br>

## Listing rules

Request:

    GET /api/rules

Response:

    200 OK

    [
        {
            "id": 1,
            "createdAt": "2016-01-01 00:00:00",
            "filter": 1,
            "enabled": true,
            "archive": false,
            "tagsToApply": [1, 2, ...]
        },
        ...
    ]

<br>

## Creating a rule

Request:

    POST /api/rules

    {
        ...
    }

Response:

    201 Created

    {
        "id": 1,
        ...
    }

<br>

## Editing a rule

Request:

    PUT /api/rules/1

    {
        ...
    }

Response:

    200 OK

    {
        "id": 1,
        ...
    }

<br>

## Deleting a rule

Request:

    DELETE /api/rules/1

Response:

    204 No Content
