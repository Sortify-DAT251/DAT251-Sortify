'use client'
import styles from "./dropDownMenu.module.css";
import React, { useState } from "react";
import { Menu, MenuItem, IconButton } from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu"; // Hamburger icon

const DropdownMenu = () => {
    const [anchorEl, setAnchorEl] = useState<HTMLElement | null>(null);
    const open = Boolean(anchorEl);

    const handleClick = (event: React.MouseEvent<HTMLElement>) => {
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
                className={styles.hamburgerButton}
            >
                <MenuIcon sx={{ color: "#0B540D" }} />
            </IconButton>

            {/* Dropdown Menu */}
            <Menu
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
                anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
                transformOrigin={{ vertical: "top", horizontal: "left" }}
                PaperProps={{
                    sx: {
                        backgroundColor: "#FFFFFF" // sett ønsket farge for dropDownMenu
                    }
                }}
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