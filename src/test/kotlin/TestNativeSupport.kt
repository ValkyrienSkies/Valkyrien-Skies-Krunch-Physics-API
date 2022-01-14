import org.junit.jupiter.api.Test
import org.valkyrienskies.physics_api_krunch.KrunchBootstrap

class TestNativeSupport {
    @Test
    fun testNativeSupportLoadSuccess() {
        KrunchBootstrap.loadNativeBinaries()
    }
}
