CREATE TABLE IF NOT EXISTS `actividadcliente` (
  `idActividad` int(11) NOT NULL AUTO_INCREMENT,
  `idCliente` int(11) NOT NULL,
  `idUsuario` int(11) NOT NULL,
  `tipoActividad` varchar(50) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `fechaActividad` datetime NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  PRIMARY KEY (`idActividad`),
  KEY `idCliente` (`idCliente`),
  KEY `idUsuario` (`idUsuario`),
  CONSTRAINT `actividadcliente_ibfk_1` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`),
  CONSTRAINT `actividadcliente_ibfk_2` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.campania
CREATE TABLE IF NOT EXISTS `campania` (
  `idCampania` int(11) NOT NULL AUTO_INCREMENT,
  `nombreCampania` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `fechaInicio` datetime NOT NULL,
  `fechaFin` datetime NOT NULL,
  `presupuesto` decimal(10,2) DEFAULT NULL,
  `canal` varchar(50) DEFAULT NULL,
  `estado` varchar(50) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  PRIMARY KEY (`idCampania`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.campaniacliente
CREATE TABLE IF NOT EXISTS `campaniacliente` (
  `idCampaniaCliente` int(11) NOT NULL AUTO_INCREMENT,
  `idCampania` int(11) NOT NULL,
  `idCliente` int(11) NOT NULL,
  `resultado` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idCampaniaCliente`),
  KEY `idCampania` (`idCampania`),
  KEY `idCliente` (`idCliente`),
  CONSTRAINT `campaniacliente_ibfk_1` FOREIGN KEY (`idCampania`) REFERENCES `campania` (`idCampania`),
  CONSTRAINT `campaniacliente_ibfk_2` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.categoria
CREATE TABLE IF NOT EXISTS `categoria` (
  `idCategoria` int(11) NOT NULL AUTO_INCREMENT,
  `nombreCategoria` varchar(100) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  PRIMARY KEY (`idCategoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.cliente
CREATE TABLE IF NOT EXISTS `cliente` (
  `idCliente` int(11) NOT NULL AUTO_INCREMENT,
  `nitCliente` varchar(20) NOT NULL,
  `nombreCliente` varchar(50) NOT NULL,
  `apellidoCliente` varchar(50) NOT NULL,
  `correoCliente` varchar(100) NOT NULL,
  `telefonoCliente` varchar(20) NOT NULL,
  `direccionCliente` varchar(150) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  PRIMARY KEY (`idCliente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.compra
CREATE TABLE IF NOT EXISTS `compra` (
  `idCompra` int(11) NOT NULL AUTO_INCREMENT,
  `fechaCompra` datetime NOT NULL,
  `totalCompra` decimal(10,2) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  `idProveedor` int(11) DEFAULT NULL,
  PRIMARY KEY (`idCompra`),
  KEY `IX_Relationship14` (`idProveedor`),
  CONSTRAINT `Relationship14` FOREIGN KEY (`idProveedor`) REFERENCES `proveedor` (`idProveedor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.cotizacion
CREATE TABLE IF NOT EXISTS `cotizacion` (
  `idCotizacion` int(11) NOT NULL AUTO_INCREMENT,
  `idCliente` int(11) NOT NULL,
  `idUsuario` int(11) NOT NULL,
  `fechaCotizacion` datetime NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `estado` varchar(50) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  PRIMARY KEY (`idCotizacion`),
  KEY `idCliente` (`idCliente`),
  KEY `idUsuario` (`idUsuario`),
  CONSTRAINT `cotizacion_ibfk_1` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`),
  CONSTRAINT `cotizacion_ibfk_2` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.descuento
CREATE TABLE IF NOT EXISTS `descuento` (
  `idDescuento` int(11) NOT NULL AUTO_INCREMENT,
  `nombreDescuento` varchar(150) NOT NULL,
  `cantidadMin` int(11) NOT NULL,
  `porcentaje` decimal(10,2) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  `idProducto` int(11) DEFAULT NULL,
  PRIMARY KEY (`idDescuento`),
  KEY `IX_Relationship25` (`idProducto`),
  CONSTRAINT `Relationship25` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.detallecompra
CREATE TABLE IF NOT EXISTS `detallecompra` (
  `idDetalleCompra` int(11) NOT NULL AUTO_INCREMENT,
  `idProducto` int(11) DEFAULT NULL,
  `cantidad` int(11) NOT NULL,
  `precioUnitario` decimal(10,2) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  `idCompra` int(11) DEFAULT NULL,
  PRIMARY KEY (`idDetalleCompra`),
  KEY `IX_Relationship17` (`idCompra`),
  KEY `IX_Relationship18` (`idProducto`),
  CONSTRAINT `Relationship17` FOREIGN KEY (`idCompra`) REFERENCES `compra` (`idCompra`),
  CONSTRAINT `Relationship18` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.detallecotizacion
CREATE TABLE IF NOT EXISTS `detallecotizacion` (
  `idDetalleCotizacion` int(11) NOT NULL AUTO_INCREMENT,
  `idCotizacion` int(11) NOT NULL,
  `idProducto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precioUnitario` decimal(10,2) NOT NULL,
  `descuento` decimal(10,2) DEFAULT 0.00,
  PRIMARY KEY (`idDetalleCotizacion`),
  KEY `idCotizacion` (`idCotizacion`),
  KEY `idProducto` (`idProducto`),
  CONSTRAINT `detallecotizacion_ibfk_1` FOREIGN KEY (`idCotizacion`) REFERENCES `cotizacion` (`idCotizacion`),
  CONSTRAINT `detallecotizacion_ibfk_2` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.detalleventa
CREATE TABLE IF NOT EXISTS `detalleventa` (
  `idDetalleVenta` int(11) NOT NULL AUTO_INCREMENT,
  `cantidad` int(11) NOT NULL,
  `precioUnitario` decimal(10,2) NOT NULL,
  `descuento` decimal(10,2) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  `idVenta` int(11) DEFAULT NULL,
  `idProducto` int(11) DEFAULT NULL,
  PRIMARY KEY (`idDetalleVenta`),
  KEY `IX_Relationship8` (`idVenta`),
  KEY `IX_Relationship23` (`idProducto`),
  CONSTRAINT `Relationship23` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`),
  CONSTRAINT `Relationship8` FOREIGN KEY (`idVenta`) REFERENCES `venta` (`idVenta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.factura
CREATE TABLE IF NOT EXISTS `factura` (
  `idFactura` int(11) NOT NULL AUTO_INCREMENT,
  `serieFactura` varchar(20) NOT NULL,
  `numeroFactura` varchar(20) NOT NULL,
  `fechaFactura` datetime NOT NULL,
  `totalFactura` decimal(10,2) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  `idVenta` int(11) DEFAULT NULL,
  PRIMARY KEY (`idFactura`),
  KEY `IX_Relationship10` (`idVenta`),
  CONSTRAINT `Relationship10` FOREIGN KEY (`idVenta`) REFERENCES `venta` (`idVenta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.facturacompra
CREATE TABLE IF NOT EXISTS `facturacompra` (
  `idFacturaCompra` int(11) NOT NULL AUTO_INCREMENT,
  `serieFactura` varchar(20) NOT NULL,
  `numeroFactura` varchar(50) NOT NULL,
  `fechaEmision` datetime NOT NULL,
  `totalFactura` decimal(10,2) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  `idCompra` int(11) DEFAULT NULL,
  PRIMARY KEY (`idFacturaCompra`),
  KEY `IX_Relationship28` (`idCompra`),
  CONSTRAINT `Relationship28` FOREIGN KEY (`idCompra`) REFERENCES `compra` (`idCompra`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.inventario
CREATE TABLE IF NOT EXISTS `inventario` (
  `idInventario` int(11) NOT NULL AUTO_INCREMENT,
  `idProducto` int(11) DEFAULT NULL,
  `lote` varchar(50) NOT NULL,
  `cantidadActual` int(11) NOT NULL,
  `fechaVencimiento` datetime NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  PRIMARY KEY (`idInventario`),
  KEY `IX_Relationship2` (`idProducto`),
  CONSTRAINT `Relationship2` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.movimiento
CREATE TABLE IF NOT EXISTS `movimiento` (
  `idMovimiento` int(11) NOT NULL AUTO_INCREMENT,
  `idInventario` int(11) DEFAULT NULL,
  `cantidad` int(11) NOT NULL,
  `fechaMovimiento` datetime NOT NULL,
  `motivo` varchar(150) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  `idUsuario` int(11) DEFAULT NULL,
  `idTipoMovimiento` int(11) DEFAULT NULL,
  `idVenta` int(11) DEFAULT NULL,
  `idCompra` int(11) DEFAULT NULL,
  `estado` tinyint(1) NOT NULL,
  PRIMARY KEY (`idMovimiento`),
  KEY `IX_Relationship4` (`idInventario`),
  KEY `IX_Relationship5` (`idUsuario`),
  KEY `IX_Relationship12` (`idTipoMovimiento`),
  KEY `IX_Relationship21` (`idVenta`),
  KEY `IX_Relationship22` (`idCompra`),
  CONSTRAINT `Relationship12` FOREIGN KEY (`idTipoMovimiento`) REFERENCES `tipomovimiento` (`idTipoMovimiento`),
  CONSTRAINT `Relationship21` FOREIGN KEY (`idVenta`) REFERENCES `venta` (`idVenta`),
  CONSTRAINT `Relationship22` FOREIGN KEY (`idCompra`) REFERENCES `compra` (`idCompra`),
  CONSTRAINT `Relationship4` FOREIGN KEY (`idInventario`) REFERENCES `inventario` (`idInventario`),
  CONSTRAINT `Relationship5` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.oportunidadventa
CREATE TABLE IF NOT EXISTS `oportunidadventa` (
  `idOportunidad` int(11) NOT NULL AUTO_INCREMENT,
  `idCliente` int(11) NOT NULL,
  `idCotizacion` int(11) DEFAULT NULL,
  `idUsuario` int(11) NOT NULL,
  `estado` varchar(50) NOT NULL,
  `probabilidad` decimal(5,2) NOT NULL,
  `valorEstimado` decimal(10,2) DEFAULT NULL,
  `fechaCierreEsperada` datetime DEFAULT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  PRIMARY KEY (`idOportunidad`),
  KEY `idCliente` (`idCliente`),
  KEY `idCotizacion` (`idCotizacion`),
  KEY `idUsuario` (`idUsuario`),
  CONSTRAINT `oportunidadventa_ibfk_1` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`),
  CONSTRAINT `oportunidadventa_ibfk_2` FOREIGN KEY (`idCotizacion`) REFERENCES `cotizacion` (`idCotizacion`),
  CONSTRAINT `oportunidadventa_ibfk_3` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.producto
CREATE TABLE IF NOT EXISTS `producto` (
  `idProducto` int(11) NOT NULL AUTO_INCREMENT,
  `nombreProducto` varchar(100) NOT NULL,
  `descripcionProducto` varchar(150) NOT NULL,
  `imagenProducto` varchar(250) NOT NULL,
  `precioVenta` decimal(10,2) NOT NULL,
  `precioCompra` decimal(10,2) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  `idCategoria` int(11) DEFAULT NULL,
  PRIMARY KEY (`idProducto`),
  KEY `IX_Relationship3` (`idCategoria`),
  CONSTRAINT `Relationship3` FOREIGN KEY (`idCategoria`) REFERENCES `categoria` (`idCategoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.proveedor
CREATE TABLE IF NOT EXISTS `proveedor` (
  `idProveedor` int(11) NOT NULL AUTO_INCREMENT,
  `nitProveedor` varchar(20) NOT NULL,
  `nombreProveedor` varchar(150) NOT NULL,
  `correoProveedor` varchar(150) NOT NULL,
  `telefonoProveedor` varchar(50) NOT NULL,
  `direccionProveedor` varchar(250) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  PRIMARY KEY (`idProveedor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.rol
CREATE TABLE IF NOT EXISTS `rol` (
  `idRol` int(11) NOT NULL AUTO_INCREMENT,
  `nombreRol` varchar(50) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  PRIMARY KEY (`idRol`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.tipomovimiento
CREATE TABLE IF NOT EXISTS `tipomovimiento` (
  `idTipoMovimiento` int(11) NOT NULL AUTO_INCREMENT,
  `nombreTipo` varchar(150) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  `estado` tinyint(1) NOT NULL,
  PRIMARY KEY (`idTipoMovimiento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.token
CREATE TABLE IF NOT EXISTS `token` (
  `idToken` int(11) NOT NULL AUTO_INCREMENT,
  `token` varchar(500) NOT NULL,
  `tipoToken` varchar(255) NOT NULL,
  `revocado` tinyint(1) NOT NULL,
  `expirado` tinyint(1) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  `idUsuario` int(11) DEFAULT NULL,
  PRIMARY KEY (`idToken`),
  KEY `IX_Relationship24` (`idUsuario`),
  CONSTRAINT `Relationship24` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.usuario
CREATE TABLE IF NOT EXISTS `usuario` (
  `idUsuario` int(11) NOT NULL AUTO_INCREMENT,
  `idRol` int(11) DEFAULT NULL,
  `nombreUsuario` varchar(50) NOT NULL,
  `contrasenaUsuario` varchar(250) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  PRIMARY KEY (`idUsuario`),
  KEY `IX_Relationship1` (`idRol`),
  CONSTRAINT `Relationship1` FOREIGN KEY (`idRol`) REFERENCES `rol` (`idRol`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- La exportación de datos fue deseleccionada.

-- Volcando estructura para tabla progra_comercial.venta
CREATE TABLE IF NOT EXISTS `venta` (
  `idVenta` int(11) NOT NULL AUTO_INCREMENT,
  `fechaVenta` datetime NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `estado` tinyint(1) NOT NULL,
  `createdAt` timestamp NOT NULL,
  `updatedAt` timestamp NOT NULL,
  `idCliente` int(11) DEFAULT NULL,
  PRIMARY KEY (`idVenta`),
  KEY `IX_Relationship6` (`idCliente`),
  CONSTRAINT `Relationship6` FOREIGN KEY (`idCliente`) REFERENCES `cliente` (`idCliente`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;