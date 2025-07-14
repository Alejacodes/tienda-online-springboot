document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('registroForm');
    const mensajeDiv = document.getElementById('mensaje');

    if (!form) return;

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const nombreInput = document.getElementById('nombre');
        const correoInput = document.getElementById('correo');
        const passwordInput = document.getElementById('password');

        const nombre = nombreInput.value.trim();
        const correo = correoInput.value.trim();
        const password = passwordInput.value.trim();

        // Reset visual
        [nombreInput, correoInput, passwordInput].forEach(input =>
            input.classList.remove('is-invalid')
        );

        // Validaciones
        if (!nombre || !correo || !password) {
            [nombreInput, correoInput, passwordInput].forEach(input => {
                if (!input.value.trim()) input.classList.add('is-invalid');
            });
            mostrarMensaje('❗ Todos los campos son obligatorios.', 'danger');
            return;
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(correo)) {
            correoInput.classList.add('is-invalid');
            mostrarMensaje('📧 Correo electrónico no válido.', 'warning');
            return;
        }

        if (password.length < 4) {
            passwordInput.classList.add('is-invalid');
            mostrarMensaje('🔒 La contraseña debe tener al menos 4 caracteres.', 'warning');
            return;
        }

        const usuario = { nombre, correo, password };

        try {
            const response = await fetch('/api/usuarios', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(usuario)
            });

            const data = await response.json();

            if (response.ok) {
                form.reset();
                mostrarMensaje('✅ Usuario registrado exitosamente. Ahora puedes iniciar sesión.', 'success');

                // 🔁 Redirección automática después de 4 segundos
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 4000);
            } else {
                mostrarMensaje(`❌ ${data?.mensaje || 'Error al registrar el usuario.'}`, 'danger');
            }
        } catch (error) {
            console.error('Error:', error);
            mostrarMensaje('🚫 Error al conectar con el servidor.', 'danger');
        }
    });

    function mostrarMensaje(mensaje, tipo) {
        mensajeDiv.innerHTML = `
      <div class="alert alert-${tipo} fade-in text-center" role="alert">
        ${mensaje}
      </div>
    `;
        setTimeout(() => {
            mensajeDiv.innerHTML = '';
        }, 4000);
    }
});