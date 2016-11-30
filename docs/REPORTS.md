# Reports API

<br>

## Listing reports

Request:

    GET /api/reports

Response:

    200 OK

    [
        {
            "id": 1,
            "createdAt": "2016-01-01 00:00:00",
            "name": "Supermarket",
            "filter": 1,
            "charts": [
                "LINE",
                "PIE"
            ]
        },
        ...
    ]

<br>

## Creating a report

Request:

    POST /api/reports

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

## Editing a report

Request:

    PUT /api/reports/1

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

## Deleting a report

Request:

    DELETE /api/reports/1

Response:

    204 No Content

<br>

## Listing report's transactions

Request:

    GET /api/reports/1/transactions
