'use client'
import React, { useState } from "react";
import { Modal, Box, Typography, Button, Grid, TextField } from "@mui/material";
import styles from "./signup.module.css";  // Import the styles

export default function SignupModal() {
    const [open, setOpen] = useState<boolean>(false);
    const [isSignup, setIsSignup] = useState<boolean>(false);
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const handleToggleForm = () => setIsSignup(!isSignup);

    return (
        <div>
            <a
                className={styles.buttonPrimary}
                onClick={handleOpen}
            >
                Sign Up / In
            </a>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby='modal-modal-title'
                aria-describedby='modal-modal-description'
            >
                <Box className={styles.modalContainer}> {/* Apply the modal container style */}
                    <Typography
                        id='modal-modal-title'
                        variant='h6'
                        component='h2'
                        className={styles.modalTitle}  {/* Apply the modal title style */}
                    >
                        {isSignup ? 'Create an account' : 'Log in to your account'}
                    </Typography>

                    <Grid container spacing={2} justifyContent='center'>
                        {isSignup ? (
                            <>
                                <Grid item xs={12} sm={6} component="div">
                                    <TextField
                                        label="Username"
                                        variant="filled"
                                        fullWidth
                                        required
                                        value={username}
                                        onChange={(e) => setUsername(e.target.value)}
                                        InputProps={{
                                            className: styles.inputField, // Apply input field styles
                                        }}
                                        InputLabelProps={{
                                            className: styles.inputLabel, // Apply input label styles
                                        }}
                                    />
                                </Grid>

                                <Grid item xs={12} sm={6} component="div">
                                    <TextField
                                        label="Email"
                                        variant="filled"
                                        fullWidth
                                        required
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                        InputProps={{
                                            className: styles.inputField, // Apply input field styles
                                        }}
                                        InputLabelProps={{
                                            className: styles.inputLabel, // Apply input label styles
                                        }}
                                    />
                                </Grid>
                            </>
                        ) : (
                            <Grid item xs={12} sm={6} component="div">
                                <TextField
                                    label="Username/Email"
                                    variant="filled"
                                    fullWidth
                                    required
                                    value={email} // Use email for both Sign Up and Sign In
                                    onChange={(e) => setEmail(e.target.value)} // Update email value only
                                    InputProps={{
                                        className: styles.inputField, // Apply input field styles
                                    }}
                                    InputLabelProps={{
                                        className: styles.inputLabel, // Apply input label styles
                                    }}
                                />
                            </Grid>
                        )}

                        {/* Password Input */}
                        <Grid item xs={12} sm={6} component="div">
                            <TextField
                                label="Password"
                                variant="filled"
                                fullWidth
                                required
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                InputProps={{
                                    className: styles.inputField, // Apply input field styles
                                }}
                                InputLabelProps={{
                                    className: styles.inputLabel, // Apply input label styles
                                }}
                            />
                        </Grid>
                    </Grid>

                    <Button
                        variant='contained'
                        className={styles.buttonPrimary}  // Apply the primary button style
                    >
                        {isSignup ? 'Sign up' : 'Sign in'}
                    </Button>

                    <Button
                        variant='text'
                        fullWidth
                        className={styles.buttonSecondary}  // Apply the secondary button style
                        onClick={handleToggleForm}
                    >
                        {isSignup ? 'Already have an account?' : "Don't have an account?"}
                    </Button>
                </Box>
            </Modal>
        </div>
    );
}
