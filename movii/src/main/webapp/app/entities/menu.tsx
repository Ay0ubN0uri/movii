import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/movie">
        <Translate contentKey="global.menu.entities.movieMovie" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/genre">
        <Translate contentKey="global.menu.entities.movieGenre" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/actor">
        <Translate contentKey="global.menu.entities.movieActor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/comment">
        <Translate contentKey="global.menu.entities.movieComment" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-movies">
        <Translate contentKey="global.menu.entities.movieUserMovies" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
