import React from "react";
import Topbar from "../Topbar";
import Sidebar from "../Sidebar";
import Content from "../Content";
import PropTypes from "prop-types";
import { makeStyles } from "@material-ui/core/styles";

const useStyles = makeStyles((theme) => ({
    root: {
        display: "flex",
    },
}));

function Layout(props) {
    const classes = useStyles();

    return (
        <div className={classes.root}>
            <Topbar />
            <Sidebar />
            <Content>{props.children}</Content>
        </div>
    );
}

Layout.propTypes = {
    children: PropTypes.any,
};

export default Layout;
