<template>
  <form id="usuarioForm" novalidate>
    <div class="form-group">
      <label for="nombre">Nombre: </label>
      <input type="text" id="nombre" name="nombre" required />
      <span class="error-message" id="error-nombre"></span>
    </div>
  </form>
  <form id="usuarioForm" novalidate>
    <div class="form-group">
      <label for="nombre">Apellidos: </label>
      <input type="text" id="nombre" name="nombre" required />
      <span class="error-message" id="error-nombre"></span>
    </div>

    <button type="submit">Crear Usuario</button>
  </form>
</template>
<style>
* body {
  color: azure;
}

.p {
  text-align: center;
  padding: 10%;
}
</style>
