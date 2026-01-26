import React, { useEffect, useState } from 'react';
import {
    View,
    Text,
    StyleSheet,
    TouchableOpacity,
    FlatList,
    ActivityIndicator,
    ScrollView
} from 'react-native';
import Toast from 'react-native-toast-message';
import axiosSetup from '../api/axiosSetup';

const formatDate = (dateVal: any) => {
    if (!dateVal) return "-";
    if (Array.isArray(dateVal)) {
        const [year, month, day, hour, minute] = dateVal;
        return new Date(year, month - 1, day, hour || 0, minute || 0).toLocaleString();
    }
    return new Date(dateVal).toLocaleString();
};

export default function UserDetails({ route, navigation }: any) {
    const { id } = route.params || {};

    const [user, setUser] = useState<any>(null);
    const [reservations, setReservations] = useState<any[]>([]);
    const [rooms, setRooms] = useState<any[]>([]);

    const [tab, setTab] = useState<'active' | 'history'>('active');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadData();
    }, [id]);

    const loadData = async () => {
        setLoading(true);
        try {
            const userRes = await axiosSetup.get(`/users/${id}`);
            setUser(userRes.data);

            const currentRes = await axiosSetup.get('/reservations');
            const userReservations = currentRes.data.filter((r: any) =>
                r.clientId === id || r.userId === id
            );
            setReservations(userReservations);

            const roomInRes = await axiosSetup.get('/rooms');
            setRooms(roomInRes.data);

        } catch (e) {
            Toast.show({ type: 'error', text1: "Błąd wczytywania danych" });
            console.error(e);
        } finally {
            setLoading(false);
        }
    };

    const getRoomName = (roomId: string) => {
        const room = rooms.find((r: any) => r.id === roomId || r.roomId === roomId || r._id === roomId);
        return room ? `${room.roomType} (${room.basePrice} PLN)` : roomId;
    };

    const activeReservations = reservations.filter((r: any) => !r.endTime);
    const endedReservations = reservations.filter((r: any) => r.endTime);
    const displayedReservations = tab === 'active' ? activeReservations : endedReservations;

    if (loading) {
        return (
            <View style={styles.centerContainer}>
                <ActivityIndicator size="large" color="#007AFF" />
                <Text style={{ marginTop: 10 }}>Ładowanie danych...</Text>
            </View>
        );
    }

    if (!user) {
        return (
            <View style={styles.centerContainer}>
                <Text>Nie znaleziono użytkownika</Text>
            </View>
        );
    }

    const renderReservationItem = ({ item }: { item: any }) => (
        <View style={styles.card}>
            <View style={styles.cardRow}>
                <Text style={styles.cardLabel}>Boisko:</Text>
                <Text style={styles.cardValue}>{getRoomName(item.roomId)}</Text>
            </View>

            <View style={styles.cardRow}>
                <Text style={styles.cardLabel}>Start:</Text>
                <Text style={styles.cardValue}>{formatDate(item.startTime)}</Text>
            </View>

            <View style={styles.cardRow}>
                <Text style={styles.cardLabel}>Koniec:</Text>
                {item.endTime ? (
                    <Text style={styles.cardValue}>{formatDate(item.endTime)}</Text>
                ) : (
                    <Text style={[styles.cardValue, { color: 'green', fontWeight: 'bold' }]}>
                        W TRAKCIE
                    </Text>
                )}
            </View>

            <View style={styles.cardRow}>
                <Text style={styles.cardLabel}>Cena:</Text>
                <Text style={styles.cardValue}>{item.price ? `${item.price} PLN` : '-'}</Text>
            </View>
        </View>
    );

    return (
        <View style={styles.container}>
            <View style={styles.headerSection}>
                <Text style={styles.userName}>{user.firstName} {user.lastName}</Text>
                <Text style={styles.userDetail}>Login: {user.login}</Text>
                <Text style={styles.userDetail}>Email: {user.email}</Text>
                <Text style={styles.userDetail}>
                    Status: <Text style={{ fontWeight: 'bold', color: user.isActive ? 'green' : 'red' }}>
                    {user.isActive ? 'AKTYWNY' : 'NIEAKTYWNY'}
                </Text>
                </Text>
            </View>

            <Text style={styles.sectionTitle}>Rezerwacje Klienta</Text>

            <View style={styles.tabContainer}>
                <TouchableOpacity
                    style={[styles.tabButton, tab === 'active' && styles.tabActive]}
                    onPress={() => setTab('active')}
                >
                    <Text style={[styles.tabText, tab === 'active' && styles.tabTextActive]}>
                        Aktualne ({activeReservations.length})
                    </Text>
                </TouchableOpacity>

                <TouchableOpacity
                    style={[styles.tabButton, tab === 'history' && styles.tabActive]}
                    onPress={() => setTab('history')}
                >
                    <Text style={[styles.tabText, tab === 'history' && styles.tabTextActive]}>
                        Zakończone ({endedReservations.length})
                    </Text>
                </TouchableOpacity>
            </View>

            <FlatList
                data={displayedReservations}
                keyExtractor={(item) => item.id || item._id || Math.random().toString()}
                renderItem={renderReservationItem}
                ListEmptyComponent={
                    <Text style={styles.emptyText}>Brak rezerwacji w tej kategorii.</Text>
                }
                contentContainerStyle={{ paddingBottom: 20 }}
            />
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#f5f5f5',
        padding: 15,
    },
    centerContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    headerSection: {
        backgroundColor: 'white',
        padding: 20,
        borderRadius: 10,
        marginBottom: 20,
        shadowColor: "#000",
        shadowOffset: { width: 0, height: 1 },
        shadowOpacity: 0.1,
        elevation: 2,
    },
    userName: {
        fontSize: 22,
        fontWeight: 'bold',
        marginBottom: 10,
        color: '#333',
    },
    userDetail: {
        fontSize: 14,
        color: '#555',
        marginBottom: 4,
    },
    sectionTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 10,
        marginLeft: 5,
    },
    tabContainer: {
        flexDirection: 'row',
        marginBottom: 15,
        backgroundColor: '#e0e0e0',
        borderRadius: 8,
        padding: 2,
    },
    tabButton: {
        flex: 1,
        paddingVertical: 10,
        alignItems: 'center',
        borderRadius: 6,
    },
    tabActive: {
        backgroundColor: 'white',
        shadowColor: "#000",
        shadowOffset: { width: 0, height: 1 },
        shadowOpacity: 0.1,
        elevation: 1,
    },
    tabText: {
        color: '#666',
        fontWeight: '600',
    },
    tabTextActive: {
        color: '#007AFF',
        fontWeight: 'bold',
    },
    card: {
        backgroundColor: 'white',
        padding: 15,
        marginBottom: 10,
        borderRadius: 8,
        borderLeftWidth: 4,
        borderLeftColor: '#007AFF',
        elevation: 1,
    },
    cardRow: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginBottom: 5,
    },
    cardLabel: {
        color: '#888',
        fontSize: 14,
    },
    cardValue: {
        color: '#333',
        fontSize: 14,
        fontWeight: '500',
    },
    emptyText: {
        textAlign: 'center',
        marginTop: 20,
        color: '#888',
        fontStyle: 'italic',
    },
});