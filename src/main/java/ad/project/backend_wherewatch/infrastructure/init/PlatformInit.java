package ad.project.backend_wherewatch.infrastructure.init;

import ad.project.backend_wherewatch.domain.models.Platform;
import ad.project.backend_wherewatch.domain.repositories.PlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 * Initializes the predefined list of streaming platforms in the database at application startup.
 * <p>
 * Implements CommandLineRunner to execute this initialization logic automatically
 * when the Spring Boot application starts.
 */
@Component
public class PlatformInit implements CommandLineRunner {

    private final PlatformRepository platformRepository;

    @Autowired
    public PlatformInit(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    /**
     * Runs on application startup to ensure the predefined platforms are present in the database.
     * If a platform with the same ID is already present, it will not be added again.
     *
     * @param args command line arguments (not used).
     */
    @Override
    public void run(String... args) {
        List<Platform> platforms = Arrays.asList(
                new Platform(8, "Netflix", "/pbpMk2JmcoNnQwx5JGpXngfoWtp.jpg"),
                new Platform(337, "Disney Plus", "/97yvRBw1GzX7fXprcF80er19ot.jpg"),
                new Platform(350, "Apple TV+", "/2E03IAZsX4ZaUqM7tXlctEPMGWS.jpg"),
                new Platform(119, "Amazon Prime Video", "/pvske1MyAoymrs5bguRfVqYiM9a.jpg"),
                new Platform(2241, "Movistar Plus+", "/jse4MOi92Jgetym7nbXFZZBI6LK.jpg"),
                new Platform(2, "Apple TV", "/9ghgSC0MA082EL6HLCW3GalykFD.jpg"),
                new Platform(149, "Movistar Plus+ Ficción Total", "/f6TRLB3H4jDpFEZ0z2KWSSvu1SB.jpg"),
                new Platform(63, "Filmin", "/kO2SWXvDCHAquaUuTJBuZkTBAuU.jpg"),
                new Platform(35, "Rakuten TV", "/bZvc9dXrXNly7cA0V4D9pR8yJwm.jpg"),
                new Platform(1773, "SkyShowtime", "/h0ZYcYHicKQ4Ixm5nOjqvwni5NG.jpg"),
                new Platform(62, "Atres Player", "/oN6g8QorcoYo3mx4BulU22ghKq4.jpg"),
                new Platform(3, "Google Play Movies", "/8z7rC8uIDaTM91X0ZfkRf04ydj2.jpg"),
                new Platform(393, "FlixOlé", "/ozMgkAAoi6aDI5ce8KKA2k8TGvB.jpg"),
                new Platform(1899, "Max", "/170ZfHTLT6ZlG38iLLpNYcBGUkG.jpg"),
                new Platform(11, "MUBI", "/fj9Y8iIMFUC6952HwxbGixTQPb7.jpg"),
                new Platform(188, "YouTube Premium", "/rMb93u1tBeErSYLv79zSTR07UdO.jpg"),
                new Platform(456, "Mitele", "/273VHxSrDdEwLTXJrsnE6yJMqNZ.jpg"),
                new Platform(541, "rtve", "/3QQKYFUDt13Q2Zm6JM2cOjlbd27.jpg"),
                new Platform(538, "Plex", "/vLZKlXUNDcZR7ilvfY9Wr9k80FZ.jpg"),
                new Platform(300, "Pluto TV", "/dB8G41Q6tSL5NBisrIeqByfepBc.jpg"),
                new Platform(283, "Crunchyroll", "/fzN5Jok5Ig1eJ7gyNGoMhnLSCfh.jpg"),
                new Platform(2285, "JustWatchTV", "/uCMLyl8jGIbInVyDeCeV6kpciFm.jpg")
        );
        for (Platform platform : platforms) {
            platformRepository.findById(platform.getId()).orElseGet(() -> platformRepository.save(platform));
        }
    }
}
