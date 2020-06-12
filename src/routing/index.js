import React from "react";
import HomeIcon from "@material-ui/icons/Home";
import ListIcon from "@material-ui/icons/List";
import FileCopyIcon from "@material-ui/icons/FileCopy";

export const sidenavItems = [
    {
        text: "Home",
        key: "home",
        route: "/",
        icon: <HomeIcon />,
    },
    {
        text: "Mapping",
        key: "mapping",
        route: "/mapping",
        icon: <ListIcon />,
    },
    {
        text: "Generate",
        key: "generate",
        route: "/generate",
        icon: <FileCopyIcon />,
    },
];
