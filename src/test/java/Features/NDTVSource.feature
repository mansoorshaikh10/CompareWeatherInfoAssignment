Feature: Compare Weather information from two different sources

  Scenario: Use Pin Your City module of NDTV Weather page to get temperature info for a city
    Given User is on the NDTV Weather page
    When User enters city name in the Pin Your City section
    Then Temperature information is available for the city on the map

  Scenario: Use City Pin on the map on NDTV Weather page to get temperature info for a city
    Given User is on the NDTV Weather page
    When User selects a city on the map
    Then Weather details is available for the city on the map
