<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:p="http://primefaces.org/ui">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resources/css/general.css"></link>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resources/css/fontStyles.css"></link>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resources/css/animate.css " />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resources/css/style.css " />
<link href="/Biometrico/img/favicon.ico" rel="icon" />
<script type="text/javascript"
	src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript"
	src="http://cdnjs.cloudflare.com/ajax/libs/gsap/1.16.1/TweenMax.min.js"></script>
<script type="text/javascript" src="/Biometrico/resources/js/wow.min.js"></script>
<script type="text/javascript">
	new WOW().init();
</script>
<title>Inicio de Sesi�n</title>
</head>
<body>
	<div class="login">

		<div class="cabecera-siiu">
			<div class="cabecera-texto">
				<h1 class="wow fadeInDown" data-wow-delay=".5s">UNIVERSIDAD
					CENTRAL DEL ECUADOR</h1>
				<h1 class="wow fadeInUp" data-wow-delay="1s">SISTEMA INTEGRAL
					DE INFORMACI�N UNIVERSITARIA</h1>
			</div>
		</div>

		<div>

			<div>
				<div id="login-button" class="wow flip" data-wow-delay=".5s">
					<i class="fa fa-uce_academico"></i>
				</div>
			</div>

			<div id="container">
				<h1 style="color: #000; margin: 40px auto auto;">INICIAR SESI�N</h1>
				<span class="close-btn"> <img
					src="https://cdn4.iconfinder.com/data/icons/miu/22/circle_close_delete_-128.png"></img>
				</span>
				<form action="/Biometrico/j_spring_security_check" method="post">
					<div class="usuario">
						<i class="fa fa-user-secret"></i>
						<input type="text" id="j_username" name="j_username" placeholder="Usuario" />
					</div>
					<div class="password">
						<i class="fa fa-key"></i> 
						<input type="password" id="j_password" name="j_password" placeholder="Contrase�a" />
					</div>
					<div class="ingresar">
						<input type="submit" value="Ingresar" />
					</div>

				</form>
			</div>
		</div>

	</div>

	<script type="text/javascript" src="/Biometrico/resources/js/theme.js"></script>

</body>
</html>