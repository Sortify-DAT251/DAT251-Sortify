'use client'
import styles from "../component/header.module.css";
import React, { useState } from "react";
import { Menu, MenuItem, IconButton } from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu"; // Hamburger icon

const DropdownMenu = () => {
    const [anchorEl, setAnchorEl] = useState(null);
    const open = Boolean(anchorEl);

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    return (
        <div>
            {/* Hamburger Menu Button */}
            <IconButton
                onClick={handleClick}
                sx={{
                    width: 40,
                    height: 40,
                    border: "2px solid black",
                    borderRadius: 1,
                    backgroundColor: "#4CAF50"
                }}
            >
                <MenuIcon sx={{ color: "black" }} />
            </IconButton>

            {/* Dropdown Menu */}
            <Menu
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
                transformOrigin={{ vertical: "top", horizontal: "left" }}
            >
                <MenuItem component="a" href="/en/" className={styles.menuItem} onClick={handleClose}>
                    Home
                </MenuItem>
                <MenuItem component="a" href="/en/stats" className={styles.menuItem} onClick={handleClose}>
                    Stats
                </MenuItem>
                <MenuItem component="a" href="https://docs.astro.build/en/develop-and-build/" className={styles.menuItem} onClick={handleClose}>
                    Documentation
                </MenuItem>
            </Menu>
        </div>
    );
};

export default DropdownMenu;
