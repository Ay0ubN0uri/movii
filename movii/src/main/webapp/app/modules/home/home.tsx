import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert, Button } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

const EntityCard = ({ title, description, linkPath, borderCard, buttonText, buttonColor }) => {
  const buttonStyle = {
    backgroundColor: buttonColor,
  };

  return (
    <Col md={6}>
      <div className="custom-card" style={{borderColor: borderCard}}>
        <h5>{title}</h5>
        <p>{description}</p>
        <Link to={linkPath}>
          <Button variant="primary" style={buttonStyle}>
            {buttonText}
          </Button>
        </Link>
      </div>
    </Col>
  );
};

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  // @ts-ignore
  return (
    <>
      {account?.login ? (
        <>
          {/*<Row className="mt-5">*/}
          {/*  <div>*/}
          {/*    <Alert color="success">*/}
          {/*      <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>*/}
          {/*        You are logged in as user {account.login}.*/}
          {/*      </Translate>*/}
          {/*    </Alert>*/}
          {/*  </div>*/}
          {/*</Row>*/}
          <Row className="mt-5">
            <Col md={12} className="text-center">
              <h1 className="display-4 mb-4">Welcome to Movii {account.login}!</h1>
              <p className="lead">Explore a world of movies tailored just for you. Dive into a collection of diverse films,
                discover your favorite actors, and enjoy an immersive cinematic experience on our unique platform.
              </p>

              <Row className="mb-4">
                <EntityCard
                  title="Movies"
                  description="Explore movies and find details about your favorite films."
                  linkPath="/movie"
                  borderCard = "#007bff"
                  buttonText="Explore Movies"
                  buttonColor="#007bff" // Blue color
                />
                <EntityCard
                  title="Genres"
                  description="Explore different movie genres."
                  linkPath="/genre"
                  borderCard = "#dc3545"
                  buttonText="Explore Genres"
                  buttonColor="#dc3545" // Red color
                />
              </Row>

              <Row className="mb-4">
                <EntityCard
                  title="Actors"
                  description="Discover information about your favorite actors."
                  linkPath="/actor"
                  borderCard = "#28a745"
                  buttonText="Explore Actors"
                  buttonColor="#28a745" // Green color
                />
                <EntityCard
                  title="Comments"
                  description="Discover information about your favorite actors."
                  linkPath="/comment"
                  borderCard = "#ffc107"
                  buttonText="Explore Comments"
                  buttonColor="#ffc107" // Yellow color
                />
              </Row>
            </Col>
          </Row>
        </>
      ) : (
        <div>
          <Alert color="warning">
            <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>

            <Link to="/login" className="alert-link">
              <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
            </Link>
            <Translate contentKey="global.messages.info.authenticated.suffix">
              , you can try the default accounts:
              <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
              <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
            </Translate>
          </Alert>

          <Alert color="warning">
            <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>&nbsp;
            <Link to="/account/register" className="alert-link">
              <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
            </Link>
          </Alert>
        </div>
      )}
    </>

    // <Row>
    //   <Col md="3" className="pad">
    //     <span className="hipster rounded" />
    //   </Col>
    //   <Col md="9">
    //     <h1 className="display-4">
    //       <Translate contentKey="home.title">Welcome, Java Hipster!</Translate>
    //     </h1>
    //     <p className="lead">
    //       <Translate contentKey="home.subtitle">This is your homepage</Translate>
    //     </p>
    //     {account?.login ? (
    //       <div>
    //         <Alert color="success">
    //           <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
    //             You are logged in as user {account.login}.
    //           </Translate>
    //         </Alert>
    //       </div>
    //     ) : (
    //       <div>
    //         <Alert color="warning">
    //           <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>
    //
    //           <Link to="/login" className="alert-link">
    //             <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
    //           </Link>
    //           <Translate contentKey="global.messages.info.authenticated.suffix">
    //             , you can try the default accounts:
    //             <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
    //             <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
    //           </Translate>
    //         </Alert>
    //
    //         <Alert color="warning">
    //           <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>&nbsp;
    //           <Link to="/account/register" className="alert-link">
    //             <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
    //           </Link>
    //         </Alert>
    //       </div>
    //     )}
    //     <p>
    //       <Translate contentKey="home.question">If you have any question on JHipster:</Translate>
    //     </p>
    //
    //     <ul>
    //       <li>
    //         <a href="https://www.jhipster.tech/" target="_blank" rel="noopener noreferrer">
    //           <Translate contentKey="home.link.homepage">JHipster homepage</Translate>
    //         </a>
    //       </li>
    //       <li>
    //         <a href="https://stackoverflow.com/tags/jhipster/info" target="_blank" rel="noopener noreferrer">
    //           <Translate contentKey="home.link.stackoverflow">JHipster on Stack Overflow</Translate>
    //         </a>
    //       </li>
    //       <li>
    //         <a href="https://github.com/jhipster/generator-jhipster/issues?state=open" target="_blank" rel="noopener noreferrer">
    //           <Translate contentKey="home.link.bugtracker">JHipster bug tracker</Translate>
    //         </a>
    //       </li>
    //       <li>
    //         <a href="https://gitter.im/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
    //           <Translate contentKey="home.link.chat">JHipster public chat room</Translate>
    //         </a>
    //       </li>
    //       <li>
    //         <a href="https://twitter.com/jhipster" target="_blank" rel="noopener noreferrer">
    //           <Translate contentKey="home.link.follow">follow @jhipster on Twitter</Translate>
    //         </a>
    //       </li>
    //     </ul>
    //
    //     <p>
    //       <Translate contentKey="home.like">If you like JHipster, do not forget to give us a star on</Translate>{' '}
    //       <a href="https://github.com/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
    //         GitHub
    //       </a>
    //       !
    //     </p>
    //   </Col>
    // </Row>
  );
};

export default Home;
