import React from "react";
import "./App.css";
import CssBaseline from "@material-ui/core/CssBaseline";
import Layout from "./components/Layout";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";

function App() {
    return (
        <div className="App">
            <Router>
                <CssBaseline />
                <Layout>
                    <Switch>
                        <Route path="/mapping">
                            <h1>Mapping</h1>
                        </Route>
                        <Route path="/generate">
                            <h1>Generate</h1>
                        </Route>
                        <Route path="/">
                            <h1>Home</h1>
                        </Route>
                    </Switch>
                </Layout>
            </Router>
        </div>
    );
}

export default App;
