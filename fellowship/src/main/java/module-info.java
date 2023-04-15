import newline.FellowshipOfTheRing;

module fellowship {
    requires adventure.api;
    provides adventure.api.Story with FellowshipOfTheRing;
}
