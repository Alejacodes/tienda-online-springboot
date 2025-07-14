document.addEventListener('DOMContentLoaded', () => {
    cargarUsuarios();
});

// Función para mostrar alertas en pantalla
function mostrarAlerta(mensaje, tipo = 'danger') {
    const alerta = document.getElementById('alerta');
    if (alerta) {
        alerta.textContent = mensaje;
        alerta.className = `alert alert-${tipo} mt-3`;
        alerta.classList.remove('d-none');

        setTimeout(() => {
            alerta.classList.add('d-none');
        }, 3000);
    }
}

// Cargar usuarios desde la API
function cargarUsuarios() {
    fetch('/api/usuarios')
        .then(response => {
            if (response.status === 401 || response.status === 403) {
                // Redirige si no está autenticado
                window.location.href = 'login.html';
                return;
            }
            if (!response.ok) {
                throw new Error('Error al cargar los usuarios');
            }
            return response.json();
        })
        .then(usuarios => {
            const tbody = document.querySelector('#tablaUsuarios tbody');
            tbody.innerHTML = '';

            usuarios.forEach(usuario => {
                const fila = document.createElement('tr');
                fila.innerHTML = `
                    <td>${usuario.id}</td>
                    <td>${usuario.nombre}</td>
                    <td>${usuario.correo}</td>
                `;
                tbody.appendChild(fila);
            });
        })
        .catch(error => {
            console.error('Error al obtener usuarios:', error);
            mostrarAlerta('No se pudieron cargar los usuarios.', 'danger');
        });
}