package com.openclassrooms.entrevoisins.events;

import com.openclassrooms.entrevoisins.model.Neighbour;

public class SeeNeighbourProfileEvent {

    /**
     * Neighbour to show
     */

    public Neighbour neighbour;

    public SeeNeighbourProfileEvent(Neighbour neighbour) {
        this.neighbour = neighbour;
    }
}
