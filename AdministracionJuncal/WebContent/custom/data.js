var servicioURL = '/AdministracionJuncal/AppServicio';

var globalDataVue = {
	menuOptions: [
		 {
	        id: '',
	        label: '',
	        icon: '',
	    }
	],
	showLeftPanel: false,
	isShowAlert: false,
	isLeftPanelCollapsed: false,
	isRightPanelCollapsed: false,
	login:{
		userName: '',
		password: '',
		logueado: false,
		invalidName: false,
		invalidPassword: false
	},
	titulo: 'Mi titulo',
	selectProveedor: '',
	proveedores: [
		{
			id: 0,
			nombre: '',
			descripcion: '',
			rut: ''
		}
	],
	edificios: [
		{
			id: 0,
			nombre: '',
			direccion: ''
		}
	],
	edificio: {
		id: 0,
		nombre: '',
		direccion: '',
		unidades: []
	},
	nuevaTransaccion: {
		monto: '',
		fecha: null,
		unidad:{
			
		},
		proveedor: {
			
		}
	}
}

