document.addEventListener('DOMContentLoaded', () => {
  mostrarNombreUsuario();
  cargarCategoriasEnFormulario();
  cargarCategoriasEnFiltro();
  cargarProductos();

  document.getElementById('filtroCategoria').addEventListener('change', cargarProductosFiltrados);
  document.getElementById('formProducto').addEventListener('submit', guardarProducto);
});

function mostrarNombreUsuario() {
  fetch('/api/usuarios/me')
    .then(res => res.json())
    .then(data => {
      const nombre = data.nombre || data.usuario?.nombre || 'Invitado';
      document.getElementById('nombreUsuario').textContent = nombre;
    })
    .catch(() => {
      document.getElementById('nombreUsuario').textContent = 'Invitado';
    });
}

function mostrarAlerta(mensaje, tipo = 'success') {
  const alerta = document.getElementById('alerta');
  alerta.textContent = mensaje;
  alerta.className = `alert alert-${tipo} fade-in text-center mt-3`;
  alerta.classList.remove('d-none');

  setTimeout(() => {
    alerta.classList.add('d-none');
  }, 3000);
}

function cargarProductos() {
  fetch('/api/productos')
    .then(res => res.json())
    .then(mostrarProductos)
    .catch(err => {
      console.error('Error al cargar productos:', err);
      mostrarAlerta('Error al cargar los productos.', 'danger');
    });
}

function mostrarProductos(productos) {
  const tbody = document.getElementById('tablaProductos');
  tbody.innerHTML = '';

  productos.forEach((producto, index) => {
    const row = `
      <tr>
        <td>${index + 1}</td>
        <td>${producto.nombre}</td>
        <td>$${producto.precio.toLocaleString()}</td>
        <td>${producto.categoria?.nombre || 'Sin categoría'}</td>
        <td>
          <button class="btn btn-sm btn-outline-warning me-2"
                  onclick="editarProducto(${producto.id}, '${producto.nombre}', ${producto.precio}, ${producto.categoria?.id || null})">
            <i class="bi bi-pencil-square"></i> Editar
          </button>
          <button class="btn btn-sm btn-outline-danger"
                  onclick="eliminarProducto(${producto.id})">
            <i class="bi bi-trash3"></i> Eliminar
          </button>
        </td>
      </tr>
    `;
    tbody.innerHTML += row;
  });
}

function cargarCategoriasEnFormulario() {
  fetch('/api/categorias')
    .then(res => res.json())
    .then(data => {
      const select = document.getElementById('categoriaProducto');
      select.innerHTML = `<option value="">Seleccione una categoría</option>`;
      data.forEach(categoria => {
        select.innerHTML += `<option value="${categoria.id}">${categoria.nombre}</option>`;
      });
    })
    .catch(err => console.error('Error cargando categorías:', err));
}

function cargarCategoriasEnFiltro() {
  fetch('/api/categorias')
    .then(res => res.json())
    .then(data => {
      const select = document.getElementById('filtroCategoria');
      select.innerHTML = `<option value="">Todas las categorías</option>`;
      data.forEach(categoria => {
        select.innerHTML += `<option value="${categoria.id}">${categoria.nombre}</option>`;
      });
    })
    .catch(err => console.error('Error cargando categorías para filtro:', err));
}

function cargarProductosFiltrados() {
  const idCategoria = document.getElementById('filtroCategoria').value;
  const url = idCategoria
    ? `/api/productos/categoria/${idCategoria}`
    : '/api/productos';

  fetch(url)
    .then(res => res.json())
    .then(mostrarProductos)
    .catch(err => {
      console.error('Error al filtrar productos:', err);
      mostrarAlerta('Error al filtrar productos.', 'danger');
    });
}

function guardarProducto(e) {
  e.preventDefault();

  const nombreInput = document.getElementById('nombreProducto');
  const precioInput = document.getElementById('precioProducto');
  const categoriaSelect = document.getElementById('categoriaProducto');

  const nombre = nombreInput.value.trim();
  const precio = parseFloat(precioInput.value.trim());
  const categoriaId = categoriaSelect.value;

  let valido = true;
  [nombreInput, precioInput, categoriaSelect].forEach(input => input.classList.remove('is-invalid'));

  if (!nombre) {
    nombreInput.classList.add('is-invalid');
    valido = false;
  }
  if (isNaN(precio)) {
    precioInput.classList.add('is-invalid');
    valido = false;
  }
  if (!categoriaId) {
    categoriaSelect.classList.add('is-invalid');
    valido = false;
  }

  if (!valido) {
    mostrarAlerta('Completa todos los campos correctamente.', 'warning');
    return;
  }

  const producto = {
    nombre,
    precio,
    categoria: { id: categoriaId }
  };

  const idProducto = e.target.dataset.id;
  const url = idProducto ? `/api/productos/${idProducto}` : '/api/productos';
  const method = idProducto ? 'PUT' : 'POST';

  fetch(url, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(producto)
  })
    .then(res => {
      if (res.ok) {
        e.target.reset();
        delete e.target.dataset.id;
        cargarProductos();
        mostrarAlerta(idProducto ? '✅ Producto actualizado.' : '✅ Producto agregado.');
      } else {
        mostrarAlerta('❌ Error al guardar el producto.', 'danger');
      }
    })
    .catch(err => {
      console.error('Error al guardar el producto:', err);
      mostrarAlerta('❌ Error de conexión.', 'danger');
    });
}

function editarProducto(id, nombre, precio, categoriaId) {
  document.getElementById('nombreProducto').value = nombre;
  document.getElementById('precioProducto').value = precio;
  document.getElementById('categoriaProducto').value = categoriaId;
  document.getElementById('formProducto').dataset.id = id;
}

function eliminarProducto(id) {
  if (confirm('¿Estás segura de que deseas eliminar este producto?')) {
    fetch(`/api/productos/${id}`, {
      method: 'DELETE'
    })
      .then(res => {
        if (res.ok) {
          cargarProductos();
          mostrarAlerta('🗑️ Producto eliminado correctamente.');
        } else {
          mostrarAlerta('❌ No se pudo eliminar el producto.', 'danger');
        }
      })
      .catch(err => {
        console.error('Error al eliminar el producto:', err);
        mostrarAlerta('❌ Error al eliminar el producto.', 'danger');
      });
  }
}

function cerrarSesion() {
  fetch('/logout', { method: 'POST' })
    .then(() => window.location.href = 'login.html?logout')
    .catch(() => alert('Error al cerrar sesión.'));
}