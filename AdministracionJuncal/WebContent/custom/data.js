var servicioURL = '/AdministracionJuncal/AppServicio';

var globalDataVue = {
	menuOptions: [
		 {
	        id: '',
	        label: '',
	        icon: '',
	    }
	],
	calendarioEsp: {
		months:{
			full: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Setiembre', 'Octubre', 'Noviembre', 'Diciembre'],
			abbreviated: ['Ene', 'Feb','Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Set', 'Oct', 'Nov', 'Dic']
		},
		days: {
			full: ['Domingo', 'Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado'],
			abbreviated: ['Dom', 'Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab'],
			initials: ['D', 'L', 'M', 'M', 'J', 'V', 'S']
		}
	},
	fechaActual: '',
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
	tablaTransacciones: {
		unidad: false,
		proveedor: false
	},
	existenTransacciones: false,
	transacciones: [
		{
			edificio: {
				
			},
			unidad: {
				nroApartamento: '',
				tipoUnidad: ''
			},
			proveedor: {
				nombre: ''
			},
			monto: '',
			fecha: '',
			nroFactura: ''
		}
	],
	nuevaTransaccion: {
		monto: '',
		invalidMonto: false,
		errorMonto: '',
		gastosComunes: '',
		fondoReserva: '',
		saldoAnterior: '',
		fecha: null,
		nroFactura: '',
		unidad:{
			
		},
		proveedor: {
			
		},
		mesLiquidacion: {
			
		},
		observacion: '',
		debePagarGC: '',
		debePagarFR: ''
	},
	filtrosPagos:{
		edificio: {
			id: 0,
			nombre: '',
			direccion: '',
			unidades: []
		},
		proveedor:{
			id: 0,
			nombre: '',
			descripcion: '',
			rut: ''
		},
		tipoTransacciones: {
			label: 'copropietario',
			value: 1,
		},
		fechaInicio: null,
		fechaFin: null,
		unidad: {
			value: 0
		}
		
	},
	tipoTransacciones: [
		{
			label: 'copropietario',
			value: 1,
		},
		{
			label: 'proveedor',
			value: 2
		}
	],
	nuevaLiquidacion: {
		mesLiquidacion: ''
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

