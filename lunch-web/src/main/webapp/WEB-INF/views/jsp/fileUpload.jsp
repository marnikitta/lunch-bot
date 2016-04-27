<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> Menu upload </title>
</head>
<body>
<div>
    <form method="POST" enctype="multipart/form-data" action="./upload">
        <table>
            <tr>
                <td><input type="file" name="file"/></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Upload"/></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
