package com.example.login_app.controller;

import com.example.login_app.entity.Usuario;
import com.example.login_app.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

// @Controller indica que esta clase maneja peticiones HTTP y retorna vistas (HTML)
@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // ------ LOGIN

    // @GetMapping("/") responde cuando el usuario entra a http://localhost:8080/
    @GetMapping("/")
    public String loginPage() {
        // Sugerencia: simplemente retornar el nombre del template HTML
        // Spring + Thymeleaf busca el archivo en resources/templates/
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,
                                @RequestParam String password,
                                HttpSession session,
                                RedirectAttributes redirectAttrs) {
        // Sugerencia:
        // 1. Llamar a usuarioService.iniciarSesion() con username y password
        // 2. Si retorna un usuario válido:
        //    - Guardar el ID y nombre en la sesión con session.setAttribute()
        //    - Redirigir al dashboard: "redirect:/dashboard"
        // 3. Si no:
        //    - Agregar mensaje de error con redirectAttrs.addFlashAttribute()
        //    - Redirigir de vuelta al login: "redirect:/"
        Optional<Usuario> usuario = usuarioService.iniciarSesion(username, password);
        if (usuario.isPresent()) {
            session.setAttribute("usuarioId", usuario.get().getId());
            session.setAttribute("usuarioName", usuario.get().getUsername());
            return "redirect:/dashboard";

        } else {
            redirectAttrs.addFlashAttribute("error", "Usuario o  contraseña incorrectos");
            return "redirect:/";
        }
    }

    // ------ REGISTER

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String procesarRegister(@RequestParam String username,
                                   @RequestParam String password,
                                   RedirectAttributes redirectAttrs) {
        // Sugerencia:
        // 1. Llamar a usuarioService.registrar()
        // 2. Si retorna true → redirigir al login con mensaje de éxito
        // 3. Si retorna false → redirigir al register con mensaje de error
        boolean exito = usuarioService.registrar(username, password);
        if (exito) {
            return "redirect:/";
        } else {
            redirectAttrs.addFlashAttribute("error", "Registro incorrecto");
            return "redirect:/register";
        }
    }

    // ------ DASHBOARD
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Sugerencia:
        // 1. Verificar que haya una sesión activa (session.getAttribute("usuarioId"))
        //    Si no hay sesión, redirigir al login para proteger la página
        // 2. Pasar el nombre del usulario al template con model.addAttribute()
        // 3. Retornar el template "dashboard"
        if (session.getAttribute("usuarioId") == null ) return "redirect:/";

        model.addAttribute("username", session.getAttribute("usuarioName"));
        return "dashboard";
    }

    // ------ EDIT

    @GetMapping("/edit")
    public String editPage(HttpSession session, Model model) {
        // Sugerencia: igual que dashboard, verificar sesión y retornar template "edit"
        if (session.getAttribute("usuarioId") == null ) return "redirect:/";
        model.addAttribute("username", session.getAttribute("usuarioName"));
        return "edit";
    }

    @PostMapping("/update-username")
    public String actualizarUsername(@RequestParam String nuevoUsername,
                                     HttpSession session,
                                     RedirectAttributes redirectAttrs) {
        // Sugerencia:
        // 1. Obtener el ID de la sesión
        // 2. Llamar a usuarioService.actualizarUsername()
        // 3. Si exitoso, actualizar también el nombre en la sesión
        // 4. Redirigir con mensaje de éxito o error
        if (session.getAttribute("usuarioId") == null ) return "redirect:/";
        Long id = (Long) session.getAttribute("usuarioId");
        boolean exito = usuarioService.actualizarUsername(id, nuevoUsername);
        if (exito) {
            redirectAttrs.addFlashAttribute("success", "Editado correctamente");
            return "redirect:/edit";
        } else {
            redirectAttrs.addFlashAttribute("error", "Error al editar");
            return "redirect:/edit";
        }
    }

    @PostMapping("/update-password")
    public String actualizarPassword(@RequestParam String passwordActual,
                                     @RequestParam String nuevaPassword,
                                     HttpSession session,
                                     RedirectAttributes redirectAttrs) {
        // Sugerencia:
        // 1. Obtener el ID de la sesión
        // 2. Llamar a usuarioService.actualizarPassword()
        // 3. Redirigir con mensaje de éxito o error
        if (session.getAttribute("usuarioId") == null ) return "redirect:/";
        Long id = (Long) session.getAttribute("usuarioId");
        boolean exito = usuarioService.actualizarPassword(id, passwordActual, nuevaPassword);
        if (exito) {
            redirectAttrs.addFlashAttribute("success", "Editado correctamente");
            return "redirect:/edit";
        }else {
            redirectAttrs.addFlashAttribute("error", "Error al editar");
            return "redirect:/edit";
        }
    }

    // ------ DELETE

    @PostMapping("/delete")
    public String eliminarCuenta(@RequestParam String password,
                                 HttpSession session,
                                 RedirectAttributes redirectAttrs) {
        // Sugerencia:
        // 1. Obtener el ID de la sesión
        // 2. Llamar a usuarioService.eliminar()
        // 3. Si exitoso: invalidar la sesión con session.invalidate() y redirigir al login
        // 4. Si falla: redirigir al dashboard con mensaje de error
        if (session.getAttribute("usuarioId") == null) return "redirect:/";
        Long id = (Long) session.getAttribute("usuarioId");
        boolean exito = usuarioService.eliminar(id, password);
        if (exito) {
            session.invalidate();
            return "redirect:/";
        } else {
            redirectAttrs.addFlashAttribute("error", "Error al borrar cuenta");
            return "redirect:/dashboard";
        }
    }

    // ------ LOGOUT

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Sugerencia: session.invalidate() borra todos los datos de la sesión
        // Luego redirigir al login
        session.invalidate();
        return "redirect:/";
    }
}
