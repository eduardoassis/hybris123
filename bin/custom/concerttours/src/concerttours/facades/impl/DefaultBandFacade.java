package concerttours.facades.impl;

import concerttours.data.BandData;
import concerttours.data.TourSummaryData;
import concerttours.enums.MusicType;
import concerttours.facades.BandFacade;
import concerttours.model.BandModel;
import concerttours.service.BandService;
import de.hybris.platform.core.model.product.ProductModel;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DefaultBandFacade implements BandFacade {

    private BandService bandService;

    @Override
    public List<BandData> getBands() {

        final List<BandModel> bandModels = bandService.getBands();
        final List<BandData> bandFacadeData = new ArrayList<>();

        for (BandModel bandModel: bandModels) {
            final BandData bandData = new BandData();
            bandData.setId(bandModel.getCode());
            bandData.setName(bandModel.getName());
            bandData.setDescription(bandModel.getHistory());
            bandData.setAlbumsSold(bandModel.getAlbumSales());
            bandFacadeData.add(bandData);
        }

        return bandFacadeData;
    }

    @Override
    public BandData getBand(String name) {

        if(name == null) {
            throw new IllegalArgumentException("Band name cannot be null");
        }

        BandModel bandModel = bandService.getBandForCode(name);
        if (bandModel == null) {
            return null;
        }

        final List<String> genres = createAListOfGenres(bandModel);
        final List<TourSummaryData> tourHistory = createAListOfTourSummaryData(bandModel);

        final BandData bandData = new BandData();
        bandData.setId(bandModel.getCode());
        bandData.setName(bandData.getName());
        bandData.setAlbumsSold(bandModel.getAlbumSales());
        bandData.setDescription(bandModel.getHistory());
        bandData.setGenres(genres);
        bandData.setTours(tourHistory);

        return bandData;
    }

    private List<String> createAListOfGenres(BandModel bandModel) {

        List<String> genres = new ArrayList<>();

        if (bandModel.getTypes() != null) {

            for (MusicType musicType: bandModel.getTypes()) {
                genres.add(musicType.getCode());
            }
        }

        return genres;
    }

    private List<TourSummaryData> createAListOfTourSummaryData(BandModel bandModel) {

        ArrayList<TourSummaryData> tourHistory = new ArrayList<>();
        if (bandModel.getTours() != null)
        {
            for (final ProductModel tour : bandModel.getTours())
            {
                final TourSummaryData summary = new TourSummaryData();
                summary.setId(tour.getCode());
                summary.setTourName(tour.getName(Locale.ENGLISH));
                // making the big assumption that all variants are concerts and ignore product catalogs
                summary.setNumberOfConcerts(Integer.toString(tour.getVariants().size()));
                tourHistory.add(summary);
            }
        }
        return tourHistory;
    }

    @Required
    public void setBandService(BandService bandService) {
        this.bandService = bandService;
    }
}
