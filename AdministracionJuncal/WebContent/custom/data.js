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
	mesesLiquidacion: [],
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
		gastosComunes: '',
		fondoReserva: '',
		fecha: null,
		unidad:{
			
		},
		proveedor: {
			
		},
		mes: {
			
		}
	},
	nuevoAnioMonto: {
		anio: '',
		monto: '',
		tipoMonto: ''
	},
	años: [
		{
			id: 1,
			label: '2019',
			value: 1
		},
		{
			id: 1,
			label: '2020',
			value: 1
		},
		{
			id: 1,
			label: '2021',
			value: 1
		}
	]
}

