# Tags API

<br>

## Listing tags

Request:

    GET /api/tags

Response:

    200 OK

    [
        {
            "id": 1,
            "name": "Supermarket",
            "color": "#ff0000"
        },
        ...
    ]

<br>

## Creating a tag

Request:

    POST /api/tags

    {
        "name": "Supermarket",
        "color": "#ff0000"
    }

Response:

    201 Created

    {
        "id": 1,
        "name": "Supermarket",
        "color": "#ff0000"
    }

<br>

## Editing a tag

Request:

    PUT /api/tags/1

    {
        "name": "Shopping",
        "color": "#00ff00"
    }

Response:

    200 OK

    {
        "id": 1,
        "name": "Shopping",
        "color": "#00ff00"
    }

<br>

## Deleting a tag

Request:

    DELETE /api/tags/1

Response:

    204 No Content
