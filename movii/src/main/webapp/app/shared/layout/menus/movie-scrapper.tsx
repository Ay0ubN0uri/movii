import React from 'react';
import { Translate } from 'react-jhipster';

import {NavItem, NavLink} from "reactstrap";
import {NavLink as Link} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export const MovieScrapperMenu = () => (
  <NavItem>
    <NavLink tag={Link} to="/movie_scrapper" className="d-flex align-items-center">
      <FontAwesomeIcon icon="plus-circle" />
      <span>
          <Translate contentKey="global.menu.movie-scrapper">MovieScrapper</Translate>
        </span>
    </NavLink>
  </NavItem>
);
