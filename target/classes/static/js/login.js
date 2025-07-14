document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('loginForm');
  const urlParams = new URLSearchParams(window.location.search);
  const mensajeDiv = document.createElement('div');
  mensajeDiv.className = 'mt-3 text-center';
  form?.after(mensajeDiv);

  // Validaciones antes de enviar
  if (form) {
    form.addEventListener('submit', (e) => {
      const email = document.getElementById('username').value.trim();
      const password = document.getElementById('password').value.trim();

      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

      if (!emailRegex.test(email)) {
        e.preventDefault();
        mostrarMensaje('📧 Por favor ingresa un correo válido.', 'warning');
        return;
      }

      if (password.length < 4) {
        e.preventDefault();
        mostrarMensaje('🔒 La contraseña debe tener al menos 4 caracteres.', 'warning');
        return;
      }

      // Si es válido, deja que el formulario se envíe
    });
  }

  // Mostrar mensajes desde la URL
  if (urlParams.has('error')) {
    mostrarMensaje('❌ Usuario o contraseña incorrectos.', 'danger');
  } else if (urlParams.has('logout')) {
    mostrarMensaje('✅ Has cerrado sesión correctamente.', 'success');
  }

  // Mostrar mensajes con Bootstrap
  function mostrarMensaje(mensaje, tipo) {
    mensajeDiv.innerHTML = `
      <div class="alert alert-${tipo}" role="alert">${mensaje}</div>
    `;
    setTimeout(() => {
      mensajeDiv.innerHTML = '';
    }, 4000);
  }
});