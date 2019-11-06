<!DOCTYPE html>
<html>
	<head>
		<title>Subcribe to Spam-o-tron 3000&trade;!</title>
	</head>
	<body>
		<h1>Subcribe to Spam-o-tron 3000&trade;!</h1>
		<p>
			We won't spam you (promise!) and you can
			<a href="main?action=unsubscribe">unsubscribe</a>
			at any time, we swear!
		</p>
		<h2><font color="red">${flash}</font></h2>
		<h2><font color="blue">${success}</font></h2>
		<form method="POST" action="main">
			<input type="hidden" name="action" value="subscribe"/>
			Email Address: <input type="text" name="email"/>
			<input type="submit" value="Spam me!"/>
		</form>
	</body>
</html>
		