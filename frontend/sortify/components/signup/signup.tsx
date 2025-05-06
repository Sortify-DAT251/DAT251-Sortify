'use client'
import styles from "./signup.module.css";
import React, { useState } from "react";
import { Modal, Box, Typography, Button, Grid, TextField } from "@mui/material";

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
                className={styles.signupButton}
                onClick={handleOpen}
            >
                Lag konto / Logg inn
            </a>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby='modal-modal-title'
                aria-describedby='modal-modal-description'
            >
                <Box className={styles.modalBox}>
                    <Typography
                        id='modal-modal-title'
                        variant='h6'
                        component='h2'
                        sx={{ marginBottom: 2, fontWeight: 600 }}
                        className={styles.modalTitle}
                    >
                        {isSignup ? 'Lag en bruker' : 'Logg inn på din bruker'}
                    </Typography>

                    <Grid container spacing={2} justifyContent='center'>
                        {isSignup ? (
                            <>
                                <Grid item xs={6}>
                                    <TextField
                                        className={styles.formField}
                                        label='Brukernavn'
                                        variant='filled'
                                        fullWidth
                                        required
                                        value={username}
                                        onChange={e => setUsername(e.target.value)}
                                    />
                                </Grid>
                                <Grid item xs={6}>
                                    <TextField
                                        className={styles.formField}
                                        label='Epost'
                                        variant='filled'
                                        fullWidth
                                        required
                                        value={email}
                                        onChange={e => setEmail(e.target.value)}
                                    />
                                </Grid>
                            </>
                        ) : (
                            <Grid item xs={6}>
                                <TextField
                                    className={styles.formField}
                                    label='Brukernavn/Epost'
                                    variant='filled'
                                    fullWidth
                                    required
                                    value={email}
                                    onChange={e => setEmail(e.target.value)}
                                />
                            </Grid>
                        )}

                        <Grid item xs={4}>
                            <TextField
                                className={styles.formField}
                                label='Passord'
                                variant='filled'
                                fullWidth
                                required
                                type='password'
                                value={password}
                                onChange={e => setPassword(e.target.value)}
                            />
                        </Grid>
                    </Grid>

                    <Button className={styles.actionButton}>
                        {isSignup ? 'Lag bruker' : 'logg inn'}
                    </Button>

                    <Button
                        className={styles.toggleButton}
                        variant='text'
                        fullWidth
                        onClick={handleToggleForm}
                    >
                        {isSignup ? 'Har du allerede en bruker?' : "Har du ikke en bruker?"}
                    </Button>
                </Box>
            </Modal>
        </div>
    );
}