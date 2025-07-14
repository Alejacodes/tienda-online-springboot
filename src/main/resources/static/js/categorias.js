document.addEventListener('DOMContentLoaded', () => {
  mostrarNombreUsuario();
  cargarCategorias();
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

function cargarCategorias() {
  fetch('/api/categorias')
    .then(res => res.json())
    .then(data => {
      const tbody = document.getElementById('tablaCategorias');
      tbody.innerHTML = '';

      data.forEach((categoria, index) => {
        const row = `
          <tr>
            <td>${index + 1}</td>
            <td>${categoria.nombre}</td>
            <td>
              <button class="btn btn-sm btn-outline-warning me-2"
                      onclick="editarCategoria(${categoria.id}, '${categoria.nombre}')">
                <i class="bi bi-pencil-square"></i> Editar
              </button>
              <button class="btn btn-sm btn-outline-danger"
                      onclick="eliminarCategoria(${categoria.id})">
                <i class="bi bi-trash3"></i> Eliminar
              </button>
            </td>
          </tr>
        `;
        tbody.innerHTML += row;
      });
    })
    .catch(err => {
      console.error('Error cargando categorías:', err);
      mostrarAlerta('Error al cargar las categorías.', 'danger');
    });
}

document.getElementById('formCategoria').addEventListener('submit', function (e) {
  e.preventDefault();

  const nombreInput = document.getElementById('nombreCategoria');
  const nombre = nombreInput.value.trim();
  const idCategoria = this.dataset.id;

  nombreInput.classList.remove('is-invalid');
  if (!nombre) {
    nombreInput.classList.add('is-invalid');
    mostrarAlerta('El nombre no puede estar vacío.', 'warning');
    return;
  }

  const categoria = { nombre };
  const url = idCategoria
    ? `/api/categorias/${idCategoria}`
    : '/api/categorias';
  const method = idCategoria ? 'PUT' : 'POST';

  fetch(url, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(categoria)
  })
    .then(res => {
      if (res.ok) {
        this.reset();
        delete this.dataset.id;
        cargarCategorias();
        mostrarAlerta(idCategoria ? '✅ Categoría actualizada.' : '✅ Categoría agregada.');
      } else {
        mostrarAlerta('❌ Error al guardar la categoría.', 'danger');
      }
    })
    .catch(err => {
      console.error('Error al guardar la categoría:', err);
      mostrarAlerta('❌ Hubo un problema al guardar la categoría.', 'danger');
    });
});

function editarCategoria(id, nombre) {
  const input = document.getElementById('nombreCategoria');
  input.value = nombre;
  input.classList.remove('is-invalid');
  document.getElementById('formCategoria').dataset.id = id;
}

function eliminarCategoria(id) {
  if (confirm('¿Estás segura de que quieres eliminar esta categoría?')) {
    fetch(`/api/categorias/${id}`, {
      method: 'DELETE'
    })
      .then(res => {
        if (res.ok) {
          cargarCategorias();
          mostrarAlerta('🗑️ Categoría eliminada exitosamente.');
        } else {
          mostrarAlerta('❌ No se pudo eliminar la categoría.', 'danger');
        }
      })
      .catch(err => {
        console.error('Error al eliminar la categoría:', err);
        mostrarAlerta('❌ Error al eliminar la categoría.', 'danger');
      });
  }
}

function cerrarSesion() {
  fetch('/logout', { method: 'POST' })
    .then(() => {
      window.location.href = 'login.html?logout';
    })
    .catch(() => {
      alert('Error al cerrar sesión.');
    });
}