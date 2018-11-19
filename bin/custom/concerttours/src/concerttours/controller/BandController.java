package concerttours.controller;

import concerttours.data.BandData;
import concerttours.facades.BandFacade;
import de.hybris.platform.catalog.CatalogVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@Controller
public class BandController {

    private final static String CATALOG_ID = "concertoursProductCatalog";
    private final static String CATALOG_VERSION_NAME = "Online";

    private CatalogVersionService catalogVersionService;
    private BandFacade bandFacade;

    @RequestMapping(value = "/bands")
    public String showBands(final Model model) {
        List<BandData> bands = bandFacade.getBands();
        model.addAttribute("bands", bands);
        return "BandList";
    }

    @RequestMapping(value = "/bands/{bandId}")
    public String showBandDetails(@PathVariable final String bandId, final Model model) throws UnsupportedEncodingException {
        catalogVersionService.setSessionCatalogVersion(CATALOG_ID, CATALOG_VERSION_NAME);
        final String decodedBandId = URLDecoder.decode(bandId, "UTf-8");
        final BandData band = bandFacade.getBand(decodedBandId);
        model.addAttribute("band", band);
        return "BandDetails";
    }

    @Autowired
    public void setCatalogVersionService(CatalogVersionService catalogVersionService) {
        this.catalogVersionService = catalogVersionService;
    }

    @Autowired
    public void setBandFacade(BandFacade bandFacade) {
        this.bandFacade = bandFacade;
    }
}
