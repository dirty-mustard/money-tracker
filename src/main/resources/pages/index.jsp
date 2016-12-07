<html>
    <head>
        <title>MoneyTracker</title>
    </head>
    <body style="width: 20%; margin: auto; margin-top: 300px;">
        <form action="/import" method="post" enctype="multipart/form-data">
            <select name="bank">
                <option value="ABN_AMRO">ABN AMRO</option>
                <option value="ING">ING</option>
                <option value="RABOBANK">Rabobank</option>
            </select>
            <input type="file" name="file">
            <input type="submit" value="Upload">
        </form>
    </body>
</html>
