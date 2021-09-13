package com.selenium.test;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.support.PageFactory;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.Reporter;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.selenium.driver.DriverFactory;
import com.selenium.pages.CambiosRecientesPage;
import com.selenium.pages.PediaPressGeneratedPage;
import com.selenium.pages.WikiCommonsPage;
import com.selenium.pages.WikiContactoPage;
import com.selenium.pages.WikiDataPage;
import com.selenium.pages.WikiHomePage;
import com.selenium.pages.WikiLibrosPage;
import com.selenium.pages.WikiMainPage;
import com.selenium.pages.WikiNewsPage;
import com.selenium.pages.WikiResultPage;
import com.selenium.pages.WikiViajesPage;
import com.selenium.pages.WikiWictionaryPage;

//import net.sourceforge.tess4j.*;

public class TestCaseWiki {
	WebDriver driver;
	WebDriverWait wait;

	@BeforeMethod
	public void IniciarBrowser(ITestContext context) {
		Reporter.log("Ir a http://wikipedia.org");
		driver = DriverFactory.LevantarBrowser(driver, context);
	}

	@AfterMethod
	public void CerrarBrowser() {
		DriverFactory.FinalizarBrowser(driver);
	}

	@Test(description = "Validar y verificar la seccion Crear una cuenta en las paginas especiales de Wikipedia")
	public void ValidarRegistroCuenta() throws Exception {
		WikiHomePage wikiHomePage = PageFactory.initElements(driver, WikiHomePage.class);
		WikiMainPage wikiMainPage = PageFactory.initElements(driver, WikiMainPage.class);
		WikiResultPage wikiResultPage = PageFactory.initElements(driver, WikiResultPage.class);
		
		wikiHomePage.AbrirPagina();
		
		wikiMainPage.PaginasEspeciales("Páginas especiales");

		wikiResultPage.CrearCuenta();
	}

	// Santi
	@Test(description = "Verificar que se permita editar un articulo en Wikipedia con el editor visual tras iniciar sesion")
	public void VyVEdicionArticulo() throws Exception {
		WikiHomePage wikiHomePage = PageFactory.initElements(driver, WikiHomePage.class);
		wikiHomePage.AbrirPagina();

		WikiResultPage wikiResultPage = PageFactory.initElements(driver, WikiResultPage.class);
		wikiResultPage.IniciarSesion();

		wikiResultPage.BuscarArticulo("Cloroformo");

		wikiResultPage.RealizarEdicion("Cachivache");

		wikiResultPage.GuardarCambios();

		wikiResultPage.RealizarBorrarEdicion("Cachivache");

		wikiResultPage.GuardarCambios();

		Assert.assertFalse(wikiResultPage.VerificarContenidoEditado("Cachivache"));

		Reporter.log("- Fin de la Prueba -");
	}

	// Bruno
	@Test(description = "Validar y verificar que Wikipedia busca lo ingresado por teclado")

	public void VyVBusquedaWiki() throws Exception {

		WikiHomePage wikiHomePage = PageFactory.initElements(driver, WikiHomePage.class);
		WikiResultPage wikiResultPage = PageFactory.initElements(driver, WikiResultPage.class);

		wikiHomePage.AbrirPagina();

		wikiResultPage.BuscarArticulo("Maven");
		Assert.assertTrue((wikiResultPage.VerificarUrl("Maven")), "No se redirigió al artículo de Maven");

	}

	// Charly
	@Test(description = "Validar y verificar que Wikipedia permite crear un Libro y el mismo pueda ser descargado desde PediaPress")
	public void VyVCreacionLibroWiki() throws Exception {
		WikiHomePage wikiHomePage = PageFactory.initElements(driver, WikiHomePage.class);
		wikiHomePage.RealizarBusqueda("Argentina");

		WikiResultPage wikiResultPage = PageFactory.initElements(driver, WikiResultPage.class);
		Assert.assertTrue((wikiResultPage.VerificarUrl("Argentina")), "No se redirigió al artículo de Argentina");

		wikiResultPage.CrearLibro();

		wikiResultPage.AgregarPagina("1");

		wikiResultPage.BuscarArticulo("Chile");

		// Tengo que esperar un momento para que pueda redirigir directamente al
		// artículo y no a los resultados de búsqueda
		Thread.sleep(2000);

		Assert.assertTrue((wikiResultPage.VerificarUrl("Chile")), "No se redirigió al artículo de Chile");

		wikiResultPage.AgregarPagina("2");

		wikiResultPage.BuscarArticulo("Estados Unidos");

		Assert.assertTrue((wikiResultPage.VerificarUrl("Estados_Unidos")),
				"No se redirigió al artículo de Estados Unidos");

		wikiResultPage.AgregarPagina("3");

		wikiResultPage.GestionarLibroPediaPress("Prueba Libro", "Esto es una Prueba");

		// Espero para que pueda terminar de cargar el libro (tiene que hacer un cargado
		// previo)
		// Si tarda más de este tiempo se considera que el test falló
		Thread.sleep(15000);

		PediaPressGeneratedPage pediaPressGeneratedPage = PageFactory.initElements(driver,
				PediaPressGeneratedPage.class);
		Assert.assertTrue((pediaPressGeneratedPage.VerificarUrl("pediapress.com")),
				"No se redirigió a la página de Pediapress");
		// Actualmente los tres artículos suman un total de 379 páginas en el libro que
		// se genera
		pediaPressGeneratedPage.AddCart("379");

		// Considero el final de la prueba cuando se llega al carrito
		Reporter.log("Verificamos que se redirigió a la página de carrito");
		Assert.assertTrue(pediaPressGeneratedPage.VerificarUrl("cart/"), "La ventana de Carrito no se muestra");

	}

	// Nacho
	@Test(description = "Verificar y Validar la busqueda de registros publicos sin seleccionar ningun radio button ni fecha")
	public void ValidarBusquedaRegistros() throws Exception {
		WikiHomePage wikiHomePage = PageFactory.initElements(driver, WikiHomePage.class);
		WikiMainPage wikiMainPage = PageFactory.initElements(driver, WikiMainPage.class);
		WikiResultPage wikiResultPage = PageFactory.initElements(driver, WikiResultPage.class);

		wikiHomePage.AbrirPagina();

		Assert.assertTrue((wikiMainPage.VerificarUrl("Wikipedia:Portada")),
				"No se redirigió a la portada de Wikipedia en Español");

		wikiMainPage.PaginasEspeciales("Páginas especiales");

		Assert.assertTrue((wikiResultPage.VerificarUrl("Especial:P%C3%A1ginasEspeciales")),
				"No se redirigió a Páginas Especiales");

		wikiResultPage.RegistrosSP();

		Assert.assertTrue((wikiResultPage.VerificarUrl("Especial:Registro")),
				"No se redirigió a Registro - Páginas Especiales");

		wikiResultPage.RealizarBusquedaRegistros();
	}

	// Juanma
	@Test(description = "Validar y verificar la estructura de la pagina principal de wikipedia")

	public void ValidarEstructuraWiki() throws Exception {

		WikiHomePage wikiHomePage = PageFactory.initElements(driver, WikiHomePage.class);
		WikiCommonsPage wikiCommonsPage = PageFactory.initElements(driver, WikiCommonsPage.class);
		WikiViajesPage wikiViajesPage = PageFactory.initElements(driver, WikiViajesPage.class);
		WikiWictionaryPage wikiWiktionaryPage = PageFactory.initElements(driver, WikiWictionaryPage.class);
		WikiLibrosPage wikiLibrosPage = PageFactory.initElements(driver, WikiLibrosPage.class);
		WikiNewsPage wikiNewsPage = PageFactory.initElements(driver, WikiNewsPage.class);
		WikiDataPage wikiDataPage = PageFactory.initElements(driver, WikiDataPage.class);

		wikiHomePage.OtherProjects("Commons");
		Assert.assertTrue((wikiCommonsPage.VerificarUrl("commons")), "No se redirigió a Commons");

		Reporter.log("volver a la pagina http://wikipedia.org ");
		driver.navigate().back();

		wikiHomePage.OtherProjects("Wikivoyage");
		Assert.assertTrue((wikiViajesPage.VerificarUrl("wikivoyage")), "No se redirigió a WikiVoyage");

		Reporter.log("volver a la pagina http://wikipedia.org ");
		driver.navigate().back();

		wikiHomePage.OtherProjects("Wiktionary");
		Assert.assertTrue((wikiWiktionaryPage.VerificarUrl("wiktionary")), "No se redirigió a Wiktionary");

		Reporter.log("volver a la pagina http://wikipedia.org ");
		driver.navigate().back();

		wikiHomePage.OtherProjects("Wikibooks");
		Assert.assertTrue((wikiLibrosPage.VerificarUrl("wikibooks")), "No se redirigió a Wikibooks");

		Reporter.log("volver a la pagina http://wikipedia.org ");
		driver.navigate().back();

		wikiHomePage.OtherProjects("Wikinews");
		Assert.assertTrue((wikiNewsPage.VerificarUrl("wikinews")), "No se redirigió a Wikinews");

		Reporter.log("volver a la pagina http://wikipedia.org ");
		driver.navigate().back();

		wikiHomePage.OtherProjects("Wikidata");
		Assert.assertTrue((wikiDataPage.VerificarUrl("wikidata")), "No se redirigió a Wikidata");

		Reporter.log("volver a la pagina http://wikipedia.org ");
		driver.navigate().back();

	}

	// Eze
	@Test(description = "Validar y verificar que Wikipedia Home Page contiene el campo de busqueda")
	public void ValidarIdioma() throws Exception {
		WikiHomePage wikiHomePage = PageFactory.initElements(driver, WikiHomePage.class);
		WikiMainPage wikiMainPage = PageFactory.initElements(driver, WikiMainPage.class);

		// Español
		wikiHomePage.AbrirPagina("es");
		Assert.assertTrue((wikiMainPage.VerificarUrl("https://es")), "No se redirigió a Wikipedia en Español");
		driver.navigate().back();

		// Ingles
		wikiHomePage.AbrirPagina("en");
		Assert.assertTrue((wikiMainPage.VerificarUrl("https://en")), "No se redirigió a Wikipedia en Inglés");
		driver.navigate().back();

		// Frances
		wikiHomePage.AbrirPagina("fr");
		Assert.assertTrue((wikiMainPage.VerificarUrl("https://fr")), "No se redirigió a Wikipedia en Francés");
		driver.navigate().back();

		// Portugues
		wikiHomePage.AbrirPagina("pt");
		Assert.assertTrue((wikiMainPage.VerificarUrl("https://pt")), "No se redirigió a Wikipedia en Portugués");
		driver.navigate().back();

		// Italiano
		wikiHomePage.AbrirPagina("it");
		Assert.assertTrue((wikiMainPage.VerificarUrl("https://it")), "No se redirigió a Wikipedia en Italiano");
		driver.navigate().back();
	}

	// Lucho
	@Test(description = "Validar y verificar que exista el boton iniciar sesion y sea redireccionado")
	public void VyVBorrarAgregarFiltro() throws Exception {
		WikiHomePage wikiHomePage = PageFactory.initElements(driver, WikiHomePage.class);
		WikiMainPage wikiMainPage = PageFactory.initElements(driver, WikiMainPage.class);
		CambiosRecientesPage cambiosRecientesPage = PageFactory.initElements(driver, CambiosRecientesPage.class);

		wikiHomePage.AbrirPagina();

		// buscar la seccion cambios recientes
		wikiMainPage.PaginasEspeciales("Cambios recientes");

		// hacer click en borra x filtro
		cambiosRecientesPage.BorrarFiltro("Quitar «Ser humano (no bot)»");
		cambiosRecientesPage.BorrarFiltro("Quitar «Ediciones de páginas»");
		cambiosRecientesPage.BorrarFiltro("Quitar «Creaciones de páginas»");
		cambiosRecientesPage.BorrarFiltro("Quitar «Acciones registradas»");

		// a;adir x filtro desde el panel de seleccion
		// Abrimos menu
		cambiosRecientesPage.AgregarFiltros();

	}
	
	//Leo
    @Test(description="Validar y verificar que este la opcion de donar y solicitar informacion")
    public void validarOpcion() throws Exception {

    WikiHomePage wikiHomePage = PageFactory.initElements(driver, WikiHomePage.class);
    WikiMainPage wikiMainPage = PageFactory.initElements(driver, WikiMainPage.class);
    WikiResultPage wikiresult = PageFactory.initElements(driver, WikiResultPage.class);

    wikiHomePage.AbrirPagina();
    wikiMainPage.VerificarUrl("Wikipedia:Portada");
    wikiMainPage.UbicarOpcionDonaciones();

    wikiresult.solicitarInfoDonacion();
    }
    
    //Julio
    @Test(description = "Validar y verificar los titulos Wikipedia Contacto y solicitudes")
    public void ValidarContacto() throws Exception {
        WikiHomePage wikiHomePage = PageFactory.initElements(driver, WikiHomePage.class);
        WikiContactoPage wikiContactoPage =PageFactory.initElements(driver, WikiContactoPage.class);

        wikiHomePage.AbrirPagina();
        wikiHomePage.BotonContacto();
        wikiContactoPage.titulo();
        wikiContactoPage.Subtitulo();
        wikiContactoPage.Subtitulo1();
        wikiContactoPage.Solicitud();
        wikiContactoPage.SolicitudTitulo();

    }

}