import React, { useState, useEffect, useCallback } from 'react';
import {
    View,
    Text,
    StyleSheet,
    FlatList,
    TouchableOpacity,
    Alert,
    ActivityIndicator
} from 'react-native';
import { useFocusEffect } from '@react-navigation/native';
import Toast from 'react-native-toast-message';
import axiosSetup from '../api/axiosSetup';

export default function ReservationList({ navigation }: any) {
    const [reservations, setReservations] = useState<any[]>([]);
    const [clients, setClients] = useState<any[]>([]);
    const [rooms, setRooms] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    useFocusEffect(
        useCallback(() => {
            loadData();
        }, [])
    );

    const loadData = async () => {
        if (reservations.length === 0) setLoading(true);

        try {
            const [resRes, resClients, resRooms] = await Promise.all([
                axiosSetup.get('/reservations'),
                axiosSetup.get('/users'),
                axiosSetup.get('/rooms')
            ]);
            setReservations(resRes.data);
            setClients(resClients.data);
            setRooms(resRooms.data);
        } catch (e) {
            console.error(e);
            Toast.show({ type: 'error', text1: "Błąd pobierania danych" });
        } finally {
            setLoading(false);
        }
    };

    const formatDate = (dateVal: any) => {
        if (!dateVal) return "";
        if (Array.isArray(dateVal)) {
            const [year, month, day, hour, minute] = dateVal;
            return new Date(year, month - 1, day, hour || 0, minute || 0).toLocaleString();
        }
        return new Date(dateVal).toLocaleString();
    };

    const getClientName = (id: string) => {
        const client = clients.find((c: any) => c.id === id || c.userId === id || c._id === id);
        return client ? `${client.firstName} ${client.lastName}` : 'Nieznany';
    };

    const getClientLogin = (id: string) => {
        const client = clients.find((c: any) => c.id === id || c.userId === id || c._id === id);
        return client ? client.login : id;
    };

    const getRoomName = (id: string) => {
        const room = rooms.find((r: any) => r.id === id || r.roomId === id || r._id === id);
        return room ? `${room.roomType}` : id;
    };

    const endReservation = (id: string) => {
        if (!id) return;

        Alert.alert(
            "Potwierdzenie",
            "Czy na pewno chcesz zakończyć rezerwację?",
            [
                { text: "Nie", style: "cancel" },
                {
                    text: "Tak",
                    onPress: async () => {
                        try {
                            await axiosSetup.post(`/reservations/${id}/end`);
                            Toast.show({ type: 'success', text1: "Zakończono rezerwację" });
                            loadData(); // Refresh list immediately
                        } catch (e: any) {
                            const msg = e.response?.data?.message || "Błąd operacji";
                            // Toast.show({type:'error', text1: e})
                            Toast.show({ type: 'error', text1: "Błąd", text2: msg });
                        }
                    }
                }
            ]
        );
    };

    const renderItem = ({ item }: { item: any }) => {
        const currentId = item.id || item._id || item.reservationId;
        const isActive = !item.endTime;

        return (
            <View style={styles.card}>
                <View style={styles.cardHeader}>
                    <View>
                        <Text style={styles.clientName}>{getClientName(item.clientId)}</Text>
                        <Text style={styles.clientLogin}>@{getClientLogin(item.clientId)}</Text>
                    </View>
                    <View style={[styles.badge, isActive ? styles.badgeActive : styles.badgeEnded]}>
                        <Text style={styles.badgeText}>{isActive ? 'AKTYWNA' : 'ZAKOŃCZONA'}</Text>
                    </View>
                </View>

                {/* Details */}
                <View style={styles.detailsContainer}>
                    <Text style={styles.detailLabel}>Boisko:</Text>
                    <Text style={styles.detailValue}>{getRoomName(item.roomId)}</Text>
                </View>

                <View style={styles.detailsContainer}>
                    <Text style={styles.detailLabel}>Start:</Text>
                    <Text style={styles.detailValue}>{formatDate(item.startTime)}</Text>
                </View>

                <View style={styles.detailsContainer}>
                    <Text style={styles.detailLabel}>Koniec:</Text>
                    <Text style={styles.detailValue}>
                        {isActive ? '---' : formatDate(item.endTime)}
                    </Text>
                </View>

                {/* Action Button */}
                {isActive && (
                    <TouchableOpacity
                        style={styles.endButton}
                        onPress={() => endReservation(currentId)}
                    >
                        <Text style={styles.endButtonText}>Zakończ Rezerwację</Text>
                    </TouchableOpacity>
                )}
            </View>
        );
    };

    return (
        <View style={styles.container}>
            <View style={styles.headerRow}>
                <Text style={styles.screenTitle}>Lista Rezerwacji</Text>
                <TouchableOpacity
                    style={styles.addButton}
                    onPress={() => navigation.navigate('ReservationNew')}
                >
                    <Text style={styles.addButtonText}>+ Nowa</Text>
                </TouchableOpacity>
            </View>

            {loading ? (
                <View style={styles.loader}>
                    <ActivityIndicator size="large" color="#007AFF" />
                </View>
            ) : (
                <FlatList
                    data={reservations}
                    keyExtractor={(item) => item.id || item._id || Math.random().toString()}
                    renderItem={renderItem}
                    contentContainerStyle={styles.listContent}
                    ListEmptyComponent={
                        <Text style={styles.emptyText}>Brak rezerwacji.</Text>
                    }
                />
            )}
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#f2f2f7',
        paddingTop: 10,
    },
    headerRow: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        paddingHorizontal: 15,
        marginBottom: 10,
    },
    screenTitle: {
        fontSize: 22,
        fontWeight: 'bold',
        color: '#333',
    },
    addButton: {
        backgroundColor: '#007AFF',
        paddingVertical: 8,
        paddingHorizontal: 15,
        borderRadius: 20,
    },
    addButtonText: {
        color: 'white',
        fontWeight: 'bold',
        fontSize: 14,
    },
    loader: {
        marginTop: 50,
    },
    listContent: {
        paddingHorizontal: 15,
        paddingBottom: 30,
    },
    emptyText: {
        textAlign: 'center',
        marginTop: 30,
        color: '#888',
        fontSize: 16,
    },
    // Card Styling
    card: {
        backgroundColor: 'white',
        borderRadius: 12,
        padding: 15,
        marginBottom: 15,
        // Shadow
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
        elevation: 3,
    },
    cardHeader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'flex-start',
        marginBottom: 12,
        borderBottomWidth: 1,
        borderBottomColor: '#f0f0f0',
        paddingBottom: 8,
    },
    clientName: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#333',
    },
    clientLogin: {
        fontSize: 12,
        color: '#888',
    },
    badge: {
        paddingVertical: 4,
        paddingHorizontal: 8,
        borderRadius: 4,
    },
    badgeActive: {
        backgroundColor: '#e8f5e9',
    },
    badgeEnded: {
        backgroundColor: '#eeeeee',
    },
    badgeText: {
        fontSize: 10,
        fontWeight: 'bold',
        color: '#2e7d32',
    },
    detailsContainer: {
        flexDirection: 'row',
        marginBottom: 6,
    },
    detailLabel: {
        width: 60,
        fontSize: 14,
        color: '#888',
        fontWeight: '500',
    },
    detailValue: {
        flex: 1,
        fontSize: 14,
        color: '#333',
    },
    endButton: {
        marginTop: 10,
        backgroundColor: '#ff3b30',
        paddingVertical: 10,
        borderRadius: 8,
        alignItems: 'center',
    },
    endButtonText: {
        color: 'white',
        fontWeight: 'bold',
        fontSize: 14,
    },
});