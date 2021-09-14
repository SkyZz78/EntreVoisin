package com.openclassrooms.entrevoisins.events;

import com.openclassrooms.entrevoisins.model.Neighbour;

public class SeeNeighbourProfilEvent {

    /**
     * Neighbour to show
     */

    public Neighbour neighbour;

    public SeeNeighbourProfilEvent(Neighbour neighbour) {
        this.neighbour = neighbour;
    }
}
