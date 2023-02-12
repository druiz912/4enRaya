# Configuración de seguridad de la aplicación de Spring Webflux

La clase _SecurityConfiguration_ es una clase de configuración que se encarga de la gestión de seguridad en la aplicación. 
Esta clase se marca con las anotaciones **@Configuration**, **@EnableWebFluxSecurity** y **@EnableReactiveMethodSecurity** 
para habilitar la seguridad web y de métodos en la aplicación de Spring Webflux.

El constructor de esta clase recibe dos dependencias: **_ReactiveUserDetailsService_** y **_PasswordEncoder_**. 
La primera dependencia es un servicio reactivo que gestiona la información de usuario y autenticación, 
y la segunda dependencia es un codificador de contraseñas.

### Método securityWebFilterChain()

Este método es un Bean que se encarga de la gestión de seguridad en la aplicación. 
Recibe un objeto ServerHttpSecurity como parámetro, que permite la configuración de seguridad en la aplicación.
Y devuelve un objeto que implementa la cadena de filtros de seguridad.

- La configuración de seguridad realizada en este método incluye:

  - **Autorización de intercambios**: permite el acceso a la página de inicio de sesión sin autenticación, 
  y requiere autenticación para todas las demás solicitudes.
  - **Inicio de sesión mediante formulario**: se especifica la URL de la página de inicio de sesión.
  - **Agregar un filtro de autenticación**: se agrega el filtro de autenticación definido en el método _webFilter()_.
  
### Método webFilter() 
Es un filtro de autenticación que se encarga de realizar la autenticación en la aplicación.
Es un Bean que devuelve un objeto _AuthenticationWebFilter_. 

- La configuración realizada en este método incluye:

  - **Establecer el controlador de éxito de autenticación**: se establece el controlador de éxito de autenticación 
  que redirige a la URL principal después de una autenticación exitosa.
  - **Asignar un gestor de autenticación**: se asigna un gestor de autenticación definido en el método reactiveAuthenticationManager().
  
### Método reactiveAuthenticationManager()

Es un gestor de autenticación que se encarga de validar las credenciales de un usuario y devolver un objeto de autenticación.
Y devuelve un objeto ReactiveAuthenticationManager.

- La configuración realizada en este método incluye la creación de un objeto
UserDetailsRepositoryReactiveAuthenticationManager, el cual es una implementación de ReactiveAuthenticationManager 
que se basa en un repositorio de información de usuario.

  - Este objeto es inicializado con la dependencia reactiveUserDetailsService que se recibe como argumento en el constructor, 
  y luego se le asigna un PasswordEncoder usando el método setPasswordEncoder().

- El objeto UserDetailsRepositoryReactiveAuthenticationManager es el encargado de validar las credenciales del usuario 
y de devolver un objeto de autenticación una vez que el usuario ha sido autenticado correctamente.

Es importante destacar que la autenticación se realiza de manera reactiva, lo que significa que se realiza en tiempo real
y no bloquea el hilo principal de ejecución.

En resumen, el método reactiveAuthenticationManager() es una parte crucial de la configuración de seguridad de la aplicación, 
ya que se encarga de validar las credenciales del usuario y de autenticarlo de manera segura y eficiente.