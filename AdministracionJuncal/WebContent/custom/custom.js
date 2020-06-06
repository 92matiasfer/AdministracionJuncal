var vm = new Vue({
	el: '#app',
	components: {
      // all Keen UI components already registered
	},
	data: globalDataVue,
	computed:{
		
	},
	methods: {
		obtenerDatosIniciales: function(){
			mui.screen.showPage('mui-screen-page', 'SLIDE_UP');
		},
		showEdificio: function(){
			mui.viewport.showPage('otra-page','NONE');
		},
		hideLeftMenu: function(){
			if(vm.isLeftPanelCollapsed){
				vm.isLeftPanelCollapsed = false;
			} else {
				vm.isLeftPanelCollapsed = true;
			}
		},
		selectItemLeftPanel: function(option, item){
			if(option.id == 1){
				mui.viewport.showPage('home-page','SLIDE_UP');
			}
			
		},
		goLogin: function(){
			var me = this;
			mui.busy(true);
			mui.ajax({
				url: servicioURL,
				type: 'GET',
				headers: {
					'Accept':'aplication/json',
					'content-type':'application/x-www-form-urlencoded'
				},
				data: {
					comando: 'login',
					userName: vm.login.userName,
					password: vm.login.password
				},
				success: function(data){
					if(data.status==0){
						iniciarSistema();
						vm.showLeftPanel = true;
						mui.viewport.showPage('home-page','NONE');
						mui.busy(false);
					} else {
						vm.login.invalidName = true;
						vm.login.invalidPassword = true;
						mui.busy(false);
						alert(data.mensaje);
					}
				},
				error: function(err,status, error){
					mui.busy(false);
					alert('Error al intentar comunicarse con el servidor');
				}
			});
		},
		goLogaut: function(){
			vm.showLeftPanel = false;
			mui.screen.showPage('login-page', 'NONE');
		},
		verEdificio: function(edificio){
			mui.busy(true);
			mui.ajax({
				url: servicioURL,
				type: 'GET',
				data: {
					comando: 'obteneredificio',
					idEdificio: edificio.id
				},
				success: function(data){
					if(data.status == 0){
						vm.edificio = data.edificio;
						mui.viewport.showPage('detalle-edificio-page');
					} else {
						alert(data.mensaje);
					}
					mui.busy(false);
				},
				error: function(err,status, error){
					mui.busy(false);
					alert('Error al intentar comunicarse con el servidor');
				}
			});
		},
		guardarTransaccion: function(){
			var me = this;
			mui.busy(true);
			var fecha = '';
			fecha = vm.nuevaTransaccion.fecha.getFullYear() + '-' + (vm.nuevaTransaccion.fecha.getMonth() + 1) + '-' + vm.nuevaTransaccion.fecha.getDate(),
			mui.ajax({
				url: servicioURL,
				type: 'GET',
				data: {
					comando: 'nuevatransaccion',
					monto: vm.nuevaTransaccion.monto,
					edificio: vm.edificio.id,
					proveedor: vm.nuevaTransaccion.proveedor.value,
					unidad: vm.nuevaTransaccion.unidad.value,
					fecha: fecha
				},
				success: function(data){
					if(data.status == 0){
						alert(data.mensaje);
					} else {
						alert(data.mensaje);
					}
					mui.busy(false);
				},
				error: function(err,status, error){
					mui.busy(false);
					alert('Error al intentar comunicarse con el servidor');
				}				
			})
		}
	}
});

//All ready!. Page &  Cordova loaded.
//Todo listo!. Página & Cordova cargados.
function deviceReady() {
	
	try {
		//Sample when Internet connection is needed but not mandatory
		//Ejemplo de cuando se necesita conexióna a Internet pero no es obligatoria.
		if (!mui.connectionAvailable()){
			if ('plugins' in window && 'toast' in window.plugins)
				mui.toast('We recommend you connect your device to the Internet');
			else
				mui.alert('We recommend you connect your device to the Internet');
		}
		
		//Hide splash.
		//Ocultar el splash.
		if (navigator.splashscreen) {
			navigator.splashscreen.hide();
		}
		
		mui.viewport.on('showpage', function(prevPageId, pageId, effect, parameters, isBack){
			if(pageId =! 'login-page'){
				vm.showLeftPanel = true;
			}
		});

		//Install events, clicks, resize, online/offline, etc. 
		installEvents();
		

	} catch (e) {
		mui.alert(e.message);
	}
}

/**
 * Install events, clicks, resize, online/offline, etc., on differents HTML elements.
 * Instala eventos, clicks, resize, online/offline, etc., sobre diferentes elementos HTML.
 */
function installEvents() {
	vm.menuOptions = [
	    {
	        id: 1,
	        label: 'Edificios',
	        icon: 'business',
	    },
	    {
	        id: 2,
	        label: 'Usuarios',
	        icon: 'group',
	    },
	    {
	        id: 3,
	        label: 'Configuracion',
	        icon: 'settings',
	    },
	    {
	        id: 4,
	        label: 'Archivos',
	        icon: 'cloud_queue',
	    },
	    {
	        id: 5,
	        label: 'Avisos',
	        icon: 'report_problem',
	    }
	];
	
	mui.util.installEvents([
		//It's a good idea to consider what happens when the device is switched on and off the internet.
		//Es buena idea considerar que pasa cuando el dispositivo se conecta y desconecta a Internet.
		{
			id: document,
			ev: 'online',
			fn: () => {
				mui.alert('online','Connection');
			}
		},
		{
			id: document,
			ev: 'offline',
			fn: () => {
				mui.alert('offline','Connection');
			}
		},
		//Typically fired when the device changes orientation.
		//Típicamente disparado cuando el dispositivo cambia de orientación.
		{
			id: window,
			ev: 'resize',
			fn: () => {
				console.log('resize');
			}
		},
		//Mail list click/touch events. See that if the event is not specified, click is assumed.
		{
			id: '.mui-backarrow',
			fn: () => {
				mui.history.back();
				return false;
			}
		},
		{
			id: '.mui-headmenu, #gomodal',
			fn: () => {
				mui.screen.showPanel('menu-panel', 'SLIDE_LEFT');	//ATENTION!!! mui.screen instead mui.viewport
				return false;
			}
		},
		{
			id: '#gocontent',
			fn: () => {
				mui.viewport.showPage('content-page','DEF');
				return false;
			}
		},
		{
			id: '#golist',
			fn: () => {
				mui.viewport.showPage('list-page','DEF');
				return false;
			}
		},
		{
			id: '#golisticon',
			fn: () => {
				return false;
			}
		},
		{
			id: '#gotrans',
			fn: () => {
				mui.viewport.showPage('transitions-page','DEF');
				return false;
			}
		},
		{
			id: '#gotest',
			fn: () => {
				mui.viewport.showPage('api-page','DEF');
				return false;
			}
		},
		//Toolbar options ------------------------------------------
		{
			id: '#tabbar-button1',
			fn: () => {
				mui.viewport.showPage('home-page', 'NONE');
				mui.history.reset();	//Look at this!
				return false;
			}
		},
		{
			id: '#tabbar-button2',
			fn: () => {
				mui.history.reset();	//Look at this!
				openInAppBrowser('http://www.mobileui.org');
				return false;
			}
		},
		{
			id: '#tabbar-button3',
			fn: () => {
				mui.history.reset();	//Look at this!
				mui.viewport.showPage('content-page', 'DEF');
				return false;
			}
		},
		{
			id: '#tabbar-button4',
			fn: () => {
				openInAppBrowser('http://www.facebook.com');
				mui.history.reset();	//Look at this!
				return false;
			}
		},
		{
			id: '#tabbar-button5',
			fn: () => {
				mui.alert('MobileUI version ' + mui.version.toString());
				mui.history.reset();	//Look at this!
				return false;
			}
		},
		{
			id: '#samplelist',
			fn: () => {
				return false;
			}
		},
		//API test options
		{
			id: '#api-cordova',
			fn: () => {
				if (mui.cordovaAvailable())
					mui.alert('Cordova/Phonegap is available!', 'Hurrah');
				else
					mui.alert('Cordova/Phonegap not available.');
				return false;
			}
		},
		{
			id: '#api-ismobile',
			fn: () => {
				if (mui.isMobileDevice.any())
					mui.alert('True', 'Hurrah');
				else
					mui.alert('False');
				return false;
			}
		},
		{
			id: '#api-vibrate',
			fn: () => {
				if (mui.cordovaAvailable())
					mui.vibrate();
				else
					mui.alert('Vibrate unavailable');
				return false;
			}
		},
		{
			id: '#api-busy',
			fn: () => {
				mui.busy(true);
				setTimeout(function() {
					mui.busy(false);
				}, 2000);
			}
		},
		{
			id: '#api-alert',
			fn: () => {
				mui.alert('Hello MUI', 'Cheers');
				return false;
			}
		},
		{
			id: '#api-confirm',
			fn: () => {
				mui.confirm('Are you happy?', function(buttonIndex) {
					mui.alert('Yo press button ' + buttonIndex, 'Result');
				},
				'Happiness',
				['Yes', 'No']
			);
			return false;
			}
		},
		{
			id: '#api-prompt',
			fn: () => {
				mui.prompt('How old are you?', function(result) {
					mui.alert('You pressed button ' + result.buttonIndex + '. Value=' + result.input1, 'Result');
				},
				'Age',
				['Ok', 'No thanks!'],
				'90'
			);
			return false;
			}
		},
		{
			id: '#api-connection',
			fn: () => {
				mui.alert(mui.getConnectionType(), 'Connection Type');
				return false;
			}
		},
		{
			id: '#api-toast',
			fn: () => {
				var msg;
				if (mui.cordovaAvailable() && mui.isMobileDevice.any())
					msg = 'This is a toast message';
				else
					msg = 'Using mui.alert() for compatibility when toast plugin are not available';
				mui.toast(msg, 'center', 'long');
				return false;
			}
		},
		{
			id: '#api-version',
			fn: () => {
				mui.alert(mui.version.toString(), 'Version');
				return false;
			}
		},
		{
			id: '#api-platform',
			fn: (currentPageId, originalTarget, event, startX, startY, endX, endY) => {
				if (mui.cordovaAvailable() && device && device.platform) {
					alert(device.model + '; ' + device.platform + ' - ' + device.version);
				}
				return false;
			}
		},
		//MobileUI viewport specific event.
		{
			vp: mui.viewport,
			ev: 'swipeleftdiscover',
			fn: () => {
				if (!mui.viewport.panelIsOpen()) {
					mui.history.back();
				}
			}
		}
	]);
	
	//Old fashion events style. Yes, of course is possible. It's a web standard!
	//jQuery
	$('#samplelist').click(function() {
		mui.alert('Nothing to do!', "Attention");
		return false;
	});
	
	//Pure javascript
	var menuOptions = document.getElementById('menuoptions')
	menuOptions.addEventListener('click', function() {
//		mui.alert('Sorry. Nothing to do!', "Attention");
		return false;
	}, false);
	
}


/**
 * Courtesy: Open an url using InAppBrowser plugin.
 * Cortesía: Abre una url usando InAppBrowser plugin.
 * @param url
 */
function openInAppBrowser(url) { 
	window.open(encodeURI(url), '_blank', 'location=yes,closebuttoncaption=Volver,presentationstyle=pagesheet,transitionstyle="fliphorizontal",EnableViewPortScale=yes');
}

/**
 * Util function to force page link to be open in InAppBrowser.
 * Función Util para forzar que los links se abran en InAppBrowser.
 * @param id
 */
function linksForInAppBrowser(pageId){
	var idd = '#'+pageId ;
	$(idd).find('a').each(function (index, element) {
		var href = $(this).attr('href');
		$(this).attr('href', '#');
		$(this).attr('target', '_self');
		$(this).attr('onclick', 'window.open("' + href + '", "_blank");');
	});
}

function iniciarSistema(){
	var me = this;
	mui.busy(true);
	mui.ajax({
		url: servicioURL,
		type: 'GET',
		data: {
			comando: 'iniciosistema',
		},
		success: function(data){
			if(data.status==0){
				vm.proveedores = data.proveedores;
				vm.edificios = data.edificios;
				mui.screen.showPage('mui-screen-page' , 'NONE');
				mui.busy(false);
			} else {
				alert(data.mensaje);
			}
		},
		error: function(err,status, error){
			mui.busy(false);
			alert('Error al intentar comunicarse con el servidor');
		}
	});
}