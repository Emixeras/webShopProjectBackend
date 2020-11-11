package de.fhdw.endpoints;

import de.fhdw.models.*;
import de.fhdw.util.PictureHandler;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@ApplicationScoped
@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Test", description = "div Test Data and basic Backend Operations")
@RegisterForReflection
public class TestImpl implements TestInterface {
    private static final Logger LOG = Logger.getLogger(TestImpl.class);
    private static final String DATEPATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    @GET
    @Transactional
    @Path("userTestData")
    @PermitAll
    @Operation(summary = "generates a few Basic User")
    public List<ShopUser> userTestData() {
        if (ShopUser.findById(1L) == null) {
            ShopUser admin = new ShopUser();
            admin.email = "admin@admin.de";
            admin.title = ShopUser.Title.BENUTZERDEFINIERT;
            admin.firstName = "Daniel";
            admin.lastName = "Pilot";
            admin.password = "Test1234";
            admin.role = ShopUser.Role.ADMIN;
            admin.birth = new Date(773560374);
            admin.street = "amselweg";
            admin.streetNumber = 123;
            admin.town = "Gütersloh";
            admin.postalCode = 33330;
            LOG.info("Benutzer angelegt: " + admin.toString());
            admin.persist();
        }
        if (ShopUser.findById(2L) == null) {
            ShopUser shopUser = new ShopUser();
            shopUser.title = ShopUser.Title.HERR;
            shopUser.email = "user@user.de";
            shopUser.password = "Test1234";
            shopUser.firstName = "Christoph";
            shopUser.lastName = "Müller";
            shopUser.birth = new Date(873560374);
            shopUser.role = ShopUser.Role.USER;
            shopUser.street = "amselweg";
            shopUser.town = "Paderborn";
            shopUser.streetNumber = 123;
            shopUser.postalCode = 33330;
            shopUser.persist();
            LOG.info("Benutzer angelegt: " + shopUser.toString());
        }
        return ShopUser.listAll();
    }

    @Override
    @GET
    @Transactional
    @Path("articleTestData")
    @Operation(summary = "generates Article Test Data")
    public List<Article> articleTestData() {
        Genre genre = new Genre("rock");
        Artist artist = new Artist("Die Ärzte");
        if (Artist.findByName("Die Ärzte") == null) {
            artist.persist();
        }
        if (Genre.findByName("rock") == null) {
            genre.persist();
        }
        if (Article.findById(1L) == null) {
            Article art1 = new Article();
            art1.description = "blub";
            art1.ean = 123;
            art1.price = 12.99;
            art1.title = "desc1";
            art1.artists = Artist.findByName("Die Ärzte");
            art1.genre = Genre.findByName("rock");
            art1.persist();
            LOG.info("added: " + art1.toString());
        }
        if (Article.findById(2L) == null) {
            Article art2 = new Article();
            art2.artists = Artist.findByName("Die Ärzte");
            art2.description = "blub";
            art2.ean = 123;
            art2.price = 12.99;
            art2.title = "desc2";
            art2.genre = Genre.findByName("rock");
            art2.persist();
            LOG.info("added: " + art2.toString());

        }
        if (Article.findById(3L) == null) {
            Article art3 = new Article();
            art3.artists = Artist.findByName("Die Ärzte");
            art3.description = "blub";
            art3.ean = 123;
            art3.price = 12.99;
            art3.title = "desc3";
            art3.genre = Genre.findByName("rock");
            art3.persist();
            LOG.info("added: " + art3.toString());

        }
        return Article.listAll();
    }

    @Override
    @GET
    @Operation(summary = "Basic Hello World Object")
    public HelloWorld get() {
        LOG.info("Dies ist der Hello World Test um "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATEPATTERN)));
        return new HelloWorld("Dies ist der Hello World Test um "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATEPATTERN)));
    }

    @POST
    @Override
    @Operation(summary = "Basic Hello World Object printed in BackendLog")
    public HelloWorld post(HelloWorld helloWorld) {
        LOG.info(helloWorld.value + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATEPATTERN)));
        return helloWorld;
    }

    @Override
    @GET
    @RolesAllowed("admin")
    @Path("admin")
    @Operation(summary = "Basic Endpoint Accepts an Admin User")
    public String getAdmin(@Context SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }

    @Override
    @GET
    @RolesAllowed("admin")
    @Path("employee")
    @Operation(summary = "Basic Endpoint Accepts an Employee User")
    public String getEmployee(@Context SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }

    @Override
    @GET
    @RolesAllowed("admin")
    @Path("user")
    @Operation(summary = "Basic Endpoint Accepts a User")
    public String getUser(@Context SecurityContext securityContext) {
        return securityContext.getUserPrincipal().getName();
    }

    @GET
    @Path("init")
    @Operation(summary = "inits TestData")
    @Transactional()
    public List<Article> Articles() throws IOException {

        final String articleFilename = "TestData/article.json";
        final String genreFilename = "Genre/genre.json";
        final String artistFilename = "TestData/artist.json";

        ClassLoader classLoader = getClass().getClassLoader();

        //put Pictures in DB
        IntStream.range(1, 11).forEach(i -> {
            String name = "TestData/Images/cat" + i + ".jpg";
            File file = new File(Objects.requireNonNull(classLoader.getResource(name)).getFile());
            LOG.debug("File Found " + i + " : " + file.exists());
            try {
                PictureHandler pictureHandler = new PictureHandler();
                Picture picture = new Picture(IOUtils.toByteArray(new FileInputStream(file)), pictureHandler.scaleImage(new FileInputStream(file), pictureHandler.checkImageFormat(new FileInputStream(file))));
                picture.persist();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //Link Genre Images
        final String genreClassic = "Genre/classic.png";
        final String genreCountry = "Genre/country.png";
        final String genreEdm = "Genre/edm.png";
        final String genreIndieRock = "Genre/IndieRock.png";
        final String genreIndustrial = "Genre/industrial.png";
        final String genreJazz = "Genre/jazz.png";
        final String genrekpop = "Genre/kpop.png";
        final String genreMetal = "Genre/metal.png";
        final String genreOldies = "Genre/oldies.png";
        final String genrePop = "Genre/pop.png";
        final String genreBlues = "Genre/blues.png";
        final String genreRap = "Genre/rap.png";
        final String genreReggae = "Genre/reggae.png";
        final String genreRock = "Genre/rock.png";
        final String genreTechno = "Genre/techno.png";
        final String genreHipHop = "Genre/hiphop.png";

        File articleFile = new File(Objects.requireNonNull(classLoader.getResource(articleFilename)).getFile());
        File genreFile = new File(Objects.requireNonNull(classLoader.getResource(genreFilename)).getFile());
        File artistFile = new File(Objects.requireNonNull(classLoader.getResource(artistFilename)).getFile());


        Jsonb jsonb = JsonbBuilder.create();
        List<Genre> genres = jsonb.fromJson(new FileInputStream(genreFile), new ArrayList<Genre>() {
        }.getClass().getGenericSuperclass());
        List<Artist> artists = jsonb.fromJson(new FileInputStream(artistFile), new ArrayList<Artist>() {
        }.getClass().getGenericSuperclass());
        List<Article> articles = jsonb.fromJson(new FileInputStream(articleFile), new ArrayList<Article>() {
        }.getClass().getGenericSuperclass());

        if (Genre.count() == 0) {
            genres.forEach(i -> {
                Genre genre = new Genre(i.name);
                genre.persist();
                LOG.debug("added Genres: " + genre.toString());
            });
            LOG.info("added Genres:" + Genre.count());
        }

        try {
            //add Classic Cover to Genre
            File fgclassic = new File(Objects.requireNonNull(classLoader.getResource(genreClassic)).getFile());
            PictureHandler pictureHandler = new PictureHandler();
            Genre classic = Genre.findByName("Classical");
            Picture picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgclassic)), pictureHandler.scaleImage(new FileInputStream(fgclassic), pictureHandler.checkImageFormat(new FileInputStream(fgclassic))));
            picture.persist();
            classic.picture = picture;

            File fgcountry = new File(Objects.requireNonNull(classLoader.getResource(genreCountry)).getFile());
            Genre country = Genre.findByName("Country");
            picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgcountry)), pictureHandler.scaleImage(new FileInputStream(fgcountry), pictureHandler.checkImageFormat(new FileInputStream(fgcountry))));
            picture.persist();
            country.picture = picture;

            File fgEDM = new File(Objects.requireNonNull(classLoader.getResource(genreEdm)).getFile());
            Genre edm = Genre.findByName("Electronic dance music");
            picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgEDM)), pictureHandler.scaleImage(new FileInputStream(fgEDM), pictureHandler.checkImageFormat(new FileInputStream(fgEDM))));
            picture.persist();
            edm.picture = picture;

            File fgHipHop = new File(Objects.requireNonNull(classLoader.getResource(genreHipHop)).getFile());
            Genre hipHop = Genre.findByName("Hip-hop");
            picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgHipHop)), pictureHandler.scaleImage(new FileInputStream(fgHipHop), pictureHandler.checkImageFormat(new FileInputStream(fgHipHop))));
            picture.persist();
            hipHop.picture = picture;

            File fgIndieRock = new File(Objects.requireNonNull(classLoader.getResource(genreIndieRock)).getFile());
            Genre indieRock = Genre.findByName("Indie rock");
            picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgIndieRock)), pictureHandler.scaleImage(new FileInputStream(fgIndieRock), pictureHandler.checkImageFormat(new FileInputStream(fgIndieRock))));
            picture.persist();
            indieRock.picture = picture;

            File fgJazz = new File(Objects.requireNonNull(classLoader.getResource(genreJazz)).getFile());
            Genre jazz = Genre.findByName("Jazz");
            picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgJazz)), pictureHandler.scaleImage(new FileInputStream(fgJazz), pictureHandler.checkImageFormat(new FileInputStream(fgJazz))));
            picture.persist();
            jazz.picture = picture;

            File fgKpop = new File(Objects.requireNonNull(classLoader.getResource(genrekpop)).getFile());
            Genre kpop = Genre.findByName("K-pop");
            picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgKpop)), pictureHandler.scaleImage(new FileInputStream(fgKpop), pictureHandler.checkImageFormat(new FileInputStream(fgKpop))));
            picture.persist();
            kpop.picture = picture;

            File fgMetal = new File(Objects.requireNonNull(classLoader.getResource(genreMetal)).getFile());
            Genre metal = Genre.findByName("Metal");
            picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgMetal)), pictureHandler.scaleImage(new FileInputStream(fgMetal), pictureHandler.checkImageFormat(new FileInputStream(fgMetal))));
            picture.persist();
            metal.picture = picture;

            File fgOldies = new File(Objects.requireNonNull(classLoader.getResource(genreOldies)).getFile());
            Genre oldies = Genre.findByName("Oldies");
            picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgOldies)), pictureHandler.scaleImage(new FileInputStream(fgOldies), pictureHandler.checkImageFormat(new FileInputStream(fgOldies))));
            picture.persist();
            oldies.picture = picture;

            File fgPop = new File(Objects.requireNonNull(classLoader.getResource(genrePop)).getFile());
            Genre pop = Genre.findByName("Pop");
            picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgPop)), pictureHandler.scaleImage(new FileInputStream(fgPop), pictureHandler.checkImageFormat(new FileInputStream(fgPop))));
            picture.persist();
            pop.picture = picture;

            File fgRap = new File(Objects.requireNonNull(classLoader.getResource(genreRap)).getFile());
            Genre Rap = Genre.findByName("Rap");
            picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgRap)), pictureHandler.scaleImage(new FileInputStream(fgRap), pictureHandler.checkImageFormat(new FileInputStream(fgRap))));
            picture.persist();
            Rap.picture = picture;

            File fgBlues = new File(Objects.requireNonNull(classLoader.getResource(genreBlues)).getFile());
            Genre rythm = Genre.findByName("Rhythm and blues");
            picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgBlues)), pictureHandler.scaleImage(new FileInputStream(fgBlues), pictureHandler.checkImageFormat(new FileInputStream(fgBlues))));
            picture.persist();
            rythm.picture = picture;

            File fgRock = new File(Objects.requireNonNull(classLoader.getResource(genreRock)).getFile());
            Genre rock = Genre.findByName("Rock");
            picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgRock)), pictureHandler.scaleImage(new FileInputStream(fgRock), pictureHandler.checkImageFormat(new FileInputStream(fgRock))));
            picture.persist();
            rock.picture = picture;

            File fgTechno = new File(Objects.requireNonNull(classLoader.getResource(genreTechno)).getFile());
            Genre techno = Genre.findByName("Techno");
            picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgTechno)), pictureHandler.scaleImage(new FileInputStream(fgTechno), pictureHandler.checkImageFormat(new FileInputStream(fgTechno))));
            picture.persist();
            techno.picture = picture;


            File fgReggae = new File(Objects.requireNonNull(classLoader.getResource(genreReggae)).getFile());
            Genre reggae = Genre.findByName("Reggae");
            picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgReggae)), pictureHandler.scaleImage(new FileInputStream(fgReggae), pictureHandler.checkImageFormat(new FileInputStream(fgReggae))));
            picture.persist();
            reggae.picture = picture;


            File fgIndustrial = new File(Objects.requireNonNull(classLoader.getResource(genreIndustrial)).getFile());
            Genre industrial = Genre.findByName("Industrial");
            picture = new Picture(IOUtils.toByteArray(new FileInputStream(fgIndustrial)), pictureHandler.scaleImage(new FileInputStream(fgIndustrial), pictureHandler.checkImageFormat(new FileInputStream(fgIndustrial))));
            picture.persist();
            industrial.picture = picture;

        } catch (Exception e) {
            LOG.info(e.toString());
        }

        if (Artist.count() == 0) {
            artists.forEach(i -> {
                Artist artist = new Artist(i.name);
                artist.persist();
                LOG.debug("added Artist: " + artist.toString());
            });
        }
        LOG.info("added Artists: " + Artist.count());
        if (Article.count() == 0) {
            articles.forEach(i -> {
                Article article = new Article();
                article.picture = Picture.findById(Integer.toUnsignedLong(ThreadLocalRandom.current().nextInt(1, 10 + 1)));
                article.title = i.title;
                article.genre = i.genre;
                article.artists = i.artists;
                article.price = i.price;
                article.description = i.description;
                article.ean = i.ean;
                article.persist();
                LOG.debug("added article: " + article.toString());
            });
            LOG.info("added Articles: " + Article.count());
        }
        return Article.listAll();
    }


    @GET
    @Path("file/{parm}")
    @Operation(summary = "gets a single file from Classpath")
    public List<String> fileTest(@PathParam("parm") String test) throws IOException {
        List<String> filenames = new ArrayList<>();

        String path = test != null ? "" : null;
        InputStream in = getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String resource;

        while ((resource = br.readLine()) != null) {
            filenames.add(resource);
        }

        return filenames;
    }


    private InputStream getResourceAsStream(String resource) {
        final InputStream in = getContextClassLoader().getResourceAsStream(resource);
        return in == null ? getClass().getResourceAsStream(resource) : in;
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
