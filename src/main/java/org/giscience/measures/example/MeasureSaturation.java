package org.giscience.measures.example;

import org.giscience.measures.rest.measure.MeasureOSHDB;
import org.giscience.measures.rest.server.OSHDBRequestParameter;
import org.giscience.measures.tools.Index;
import org.giscience.measures.tools.Lineage;
import org.giscience.utils.geogrid.cells.GridCell;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.MapAggregator;
import org.heigit.bigspatialdata.oshdb.api.object.OSMEntitySnapshot;
import org.heigit.bigspatialdata.oshdb.util.geometry.Geo;

import java.util.SortedMap;

public class MeasureSaturation extends MeasureOSHDB<Number, OSMEntitySnapshot> {

    @Override
    public Boolean refersToTimeSpan() {
        return true;
    }

    @Override
    public SortedMap<GridCell, Number> compute(MapAggregator<GridCell, OSMEntitySnapshot> mapReducer, OSHDBRequestParameter p) throws Exception {
        return Index.reduce(
                mapReducer
                        .osmTag("highway", "residential")
                        .aggregateByTimestamp(snapshot -> snapshot.getTimestamp())
                        .map(snapshot -> Geo.lengthOf(snapshot.getGeometry()))
                        .sum(),
                Lineage::saturation
        );
    }
}
