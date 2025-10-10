-- Fecha creación: 19/07/2025
-- MariaDB: 27/07/2025

-- Tabla Cliente
CREATE TABLE Cliente (
  idCliente INT NOT NULL AUTO_INCREMENT,
  nitCliente VARCHAR(20) NOT NULL,
  nombreCliente VARCHAR(50) NOT NULL,
  apellidoCliente VARCHAR(50) NOT NULL,
  correoCliente VARCHAR(100) NOT NULL,
  telefonoCliente VARCHAR(20) NOT NULL,
  direccionCliente VARCHAR(150) NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  PRIMARY KEY (idCliente)
);

-- Tabla Rol
CREATE TABLE Rol (
  idRol INT NOT NULL AUTO_INCREMENT,
  nombreRol VARCHAR(50) NOT NULL,
  estado BOOLEAN NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  PRIMARY KEY (idRol)
);

-- Tabla Usuario
CREATE TABLE Usuario (
  idUsuario INT NOT NULL AUTO_INCREMENT,
  idRol INT,
  nombreUsuario VARCHAR(50) NOT NULL,
  contrasenaUsuario VARCHAR(250) NOT NULL,
  estado BOOLEAN NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  PRIMARY KEY (idUsuario),
  INDEX IX_Relationship1 (idRol)
);

-- Tabla Categoria
CREATE TABLE Categoria (
  idCategoria INT NOT NULL AUTO_INCREMENT,
  nombreCategoria VARCHAR(100) NOT NULL,
  estado BOOLEAN NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  PRIMARY KEY (idCategoria)
);

-- Tabla Producto
CREATE TABLE Producto (
  idProducto INT NOT NULL AUTO_INCREMENT,
  nombreProducto VARCHAR(100) NOT NULL,
  descripcionProducto VARCHAR(150) NOT NULL,
  imagenProducto VARCHAR(250) NOT NULL,
  precioVenta DECIMAL(10,2) NOT NULL,
  precioCompra DECIMAL(10,2) NOT NULL,
  estado BOOLEAN NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  idCategoria INT,
  PRIMARY KEY (idProducto),
  INDEX IX_Relationship3 (idCategoria)
);

-- Tabla Inventario
CREATE TABLE Inventario (
  idInventario INT NOT NULL AUTO_INCREMENT,
  idProducto INT,
  lote VARCHAR(50) NOT NULL,
  cantidadActual INT NOT NULL,
  fechaVencimiento DATETIME NOT NULL,
  estado BOOLEAN NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  PRIMARY KEY (idInventario),
  INDEX IX_Relationship2 (idProducto)
);

-- Tabla TipoMovimiento
CREATE TABLE TipoMovimiento (
  idTipoMovimiento INT NOT NULL AUTO_INCREMENT,
  nombreTipo VARCHAR(150) NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  estado BOOLEAN NOT NULL,
  PRIMARY KEY (idTipoMovimiento)
);

-- Tabla Movimiento
CREATE TABLE Movimiento (
  idMovimiento INT NOT NULL AUTO_INCREMENT,
  idInventario INT,
  cantidad INT NOT NULL,
  fechaMovimiento DATETIME NOT NULL,
  motivo VARCHAR(150) NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  idUsuario INT,
  idTipoMovimiento INT,
  idVenta INT,
  idCompra INT,
  estado BOOLEAN NOT NULL,
  PRIMARY KEY (idMovimiento),
  INDEX IX_Relationship4 (idInventario),
  INDEX IX_Relationship5 (idUsuario),
  INDEX IX_Relationship12 (idTipoMovimiento),
  INDEX IX_Relationship21 (idVenta),
  INDEX IX_Relationship22 (idCompra)
);

-- Tabla Venta
CREATE TABLE Venta (
  idVenta INT NOT NULL AUTO_INCREMENT,
  fechaVenta DATETIME NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  estado BOOLEAN NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  idCliente INT,
  PRIMARY KEY (idVenta),
  INDEX IX_Relationship6 (idCliente)
);

-- Tabla DetalleVenta
CREATE TABLE DetalleVenta (
  idDetalleVenta INT NOT NULL AUTO_INCREMENT,
  cantidad INT NOT NULL,
  precioUnitario DECIMAL(10,2) NOT NULL,
  descuento DECIMAL(10,2) NOT NULL,
  estado BOOLEAN NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  idVenta INT,
  idProducto INT,
  PRIMARY KEY (idDetalleVenta),
  INDEX IX_Relationship8 (idVenta),
  INDEX IX_Relationship23 (idProducto)
);

-- Tabla Factura
CREATE TABLE Factura (
  idFactura INT NOT NULL AUTO_INCREMENT,
  serieFactura VARCHAR(20) NOT NULL,
  numeroFactura VARCHAR(20) NOT NULL,
  fechaFactura DATETIME NOT NULL,
  totalFactura DECIMAL(10,2) NOT NULL,
  estado BOOLEAN NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  idVenta INT,
  PRIMARY KEY (idFactura),
  INDEX IX_Relationship10 (idVenta)
);

-- Tabla Descuento
CREATE TABLE Descuento (
  idDescuento INT NOT NULL AUTO_INCREMENT,
  nombreDescuento VARCHAR(150) NOT NULL,
  cantidadMin INT NOT NULL,
  porcentaje DECIMAL(10,2) NOT NULL,
  estado BOOLEAN NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  idProducto INT,
  PRIMARY KEY (idDescuento),
  INDEX IX_Relationship25 (idProducto)
);

-- Tabla Proveedor
CREATE TABLE Proveedor (
  idProveedor INT NOT NULL AUTO_INCREMENT,
  nitProveedor VARCHAR(20) NOT NULL,
  nombreProveedor VARCHAR(150) NOT NULL,
  correoProveedor VARCHAR(150) NOT NULL,
  telefonoProveedor VARCHAR(50) NOT NULL,
  direccionProveedor VARCHAR(250) NOT NULL,
  estado BOOLEAN NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  PRIMARY KEY (idProveedor)
);

-- Tabla Compra
CREATE TABLE Compra (
  idCompra INT NOT NULL AUTO_INCREMENT,
  fechaCompra DATETIME NOT NULL,
  totalCompra DECIMAL(10,2) NOT NULL,
  estado BOOLEAN NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  idProveedor INT,
  PRIMARY KEY (idCompra),
  INDEX IX_Relationship14 (idProveedor)
);

-- Tabla DetalleCompra
CREATE TABLE DetalleCompra (
  idDetalleCompra INT NOT NULL AUTO_INCREMENT,
  idProducto INT,
  cantidad INT NOT NULL,
  precioUnitario DECIMAL(10,2) NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  idCompra INT,
  PRIMARY KEY (idDetalleCompra),
  INDEX IX_Relationship17 (idCompra),
  INDEX IX_Relationship18 (idProducto)
);

-- Tabla FacturaCompra
CREATE TABLE FacturaCompra (
  idFacturaCompra INT NOT NULL AUTO_INCREMENT,
  serieFactura VARCHAR(20) NOT NULL,
  numeroFactura VARCHAR(50) NOT NULL,
  fechaEmision DATETIME NOT NULL,
  totalFactura DECIMAL(10,2) NOT NULL,
  estado BOOLEAN NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  idCompra INT,
  PRIMARY KEY (idFacturaCompra),
  INDEX IX_Relationship28 (idCompra)
);

-- Tabla Token
CREATE TABLE Token (
  idToken INT NOT NULL AUTO_INCREMENT,
  token VARCHAR(500) NOT NULL,
  tipoToken VARCHAR(255) NOT NULL,
  revocado BOOLEAN NOT NULL,
  expirado BOOLEAN NOT NULL,
  createdAt TIMESTAMP NOT NULL,
  updatedAt TIMESTAMP NOT NULL,
  idUsuario INT,
  PRIMARY KEY (idToken),
  INDEX IX_Relationship24 (idUsuario)
);

-- Relaciones (Foreign Keys)
ALTER TABLE Usuario ADD CONSTRAINT Relationship1 FOREIGN KEY (idRol) REFERENCES Rol(idRol) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE Inventario ADD CONSTRAINT Relationship2 FOREIGN KEY (idProducto) REFERENCES Producto(idProducto) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE Producto ADD CONSTRAINT Relationship3 FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE Movimiento ADD CONSTRAINT Relationship4 FOREIGN KEY (idInventario) REFERENCES Inventario(idInventario) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE Movimiento ADD CONSTRAINT Relationship5 FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE Venta ADD CONSTRAINT Relationship6 FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE DetalleVenta ADD CONSTRAINT Relationship8 FOREIGN KEY (idVenta) REFERENCES Venta(idVenta) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE Factura ADD CONSTRAINT Relationship10 FOREIGN KEY (idVenta) REFERENCES Venta(idVenta) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE Movimiento ADD CONSTRAINT Relationship12 FOREIGN KEY (idTipoMovimiento) REFERENCES TipoMovimiento(idTipoMovimiento) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE Compra ADD CONSTRAINT Relationship14 FOREIGN KEY (idProveedor) REFERENCES Proveedor(idProveedor) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE DetalleCompra ADD CONSTRAINT Relationship17 FOREIGN KEY (idCompra) REFERENCES Compra(idCompra) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE DetalleCompra ADD CONSTRAINT Relationship18 FOREIGN KEY (idProducto) REFERENCES Producto(idProducto) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE Movimiento ADD CONSTRAINT Relationship21 FOREIGN KEY (idVenta) REFERENCES Venta(idVenta) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE Movimiento ADD CONSTRAINT Relationship22 FOREIGN KEY (idCompra) REFERENCES Compra(idCompra) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE DetalleVenta ADD CONSTRAINT Relationship23 FOREIGN KEY (idProducto) REFERENCES Producto(idProducto) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE Token ADD CONSTRAINT Relationship24 FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE Descuento ADD CONSTRAINT Relationship25 FOREIGN KEY (idProducto) REFERENCES Producto(idProducto) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE FacturaCompra ADD CONSTRAINT Relationship28 FOREIGN KEY (idCompra) REFERENCES Compra(idCompra) ON DELETE RESTRICT ON UPDATE RESTRICT;
