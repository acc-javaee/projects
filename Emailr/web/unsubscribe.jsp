<!DOCTYPE html>
<html>
	<head>
		<title>Unsubscribe from Spam-o-tron 3000&trade;!</title>
	</head>
	<body>
		<h1>Unsubcribe from Spam-o-tron 3000&trade;!</h1>
		<p>
			We're sorry to hear you want to unsubscribe but,
			as we promised, you can do so here.
		</p>
		<h2><font color="red">${flash}</font></h2>
		<h2><font color="blue">${success}</font></h2>
		<form method="POST" action="main">
			<input type="hidden" name="action" value="unsubscribe"/>
			Email Address: <input type="text" name="email"/>
			<input type="submit" value="Stop Spammin' me!"/>
		</form>
	</body>
</html>
		