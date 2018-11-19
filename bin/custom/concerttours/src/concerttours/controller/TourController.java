package concerttours.controller;

import concerttours.data.TourData;
import concerttours.facades.TourFacade;
import de.hybris.platform.catalog.CatalogVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Controller
public class TourController {

    private static final String CATALOG_ID = "concertoursProductCatalog";
    private static final String CATALOG_VERSION_NAME = "Online";
    private CatalogVersionService catalogVersionService;
    private TourFacade tourFacade;

    @RequestMapping(value = "/tours/{tourId}")
    public String showTourDetails(@PathVariable final String tourId, final Model model) throws UnsupportedEncodingException {
        catalogVersionService.setSessionCatalogVersion(CATALOG_ID, CATALOG_VERSION_NAME);
        final String decodedId = URLDecoder.decode(tourId, "UTF-8");
        final TourData tourData = tourFacade.getTourDetails(decodedId);
        model.addAttribute("tour", tourData);
        return "TourDetails";
    }

    @Autowired
    public void setCatalogVersionService(CatalogVersionService catalogVersionService) {
        this.catalogVersionService = catalogVersionService;
    }

    @Autowired
    public void setTourFacade(TourFacade tourFacade) {
        this.tourFacade = tourFacade;
    }
}
