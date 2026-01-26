import React, { useEffect, useState } from 'react';
import {
    View,
    Text,
    TextInput,
    FlatList,
    TouchableOpacity,
    StyleSheet,
    Alert
} from 'react-native';
import Toast from 'react-native-toast-message';
import axiosSetup from '../api/axiosSetup';

interface User {
    id?: string;
    userId?: string;
    login: string;
    firstName: string;
    lastName: string;
    email: string;
    isActive: boolean;
}

export default function UserList({ navigation }: any) {
    const [users, setUsers] = useState<User[]>([]);
    const [search, setSearch] = useState('');

    const fetchUsers = async () => {
        try {
            const url = search ? `/users/search?login=${search}` : '/users';
            const res: any = await axiosSetup.get(url);

            const data = res.data.map((u: any) => ({
                ...u,
                id: u.id || u.userId
            }));
            setUsers(data);
        } catch (e) {
            Toast.show({ type: 'error', text1: "Błąd podczas pobierania listy" });
        }
    };

    useEffect(() => {
        const unsubscribe = navigation.addListener('focus', () => {
            fetchUsers();
        });
        return unsubscribe;
    }, [navigation]);

    const toggleStatus = (id: string, isActive: boolean) => {
        Alert.alert(
            "Potwierdzenie",
            "Czy chcesz na pewno zmienić status użytkownika?",
            [
                { text: "Nie", style: "cancel" },
                {
                    text: "Tak",
                    onPress: async () => {
                        try {
                            await axiosSetup.post(`/users/${id}/${isActive ? 'deactivate' : 'activate'}`);
                            fetchUsers();
                            Toast.show({ type: 'success', text1: "Zmieniono status" });
                        } catch (e) {
                            Toast.show({ type: 'error', text1: "Nie udało się zmienić statusu" });
                        }
                    }
                }
            ]
        );
    };

    const renderUserItem = ({ item }: { item: User }) => (
        <View style={styles.card}>
            <View style={styles.cardHeader}>
                <Text style={styles.loginText}>{item.login}</Text>
                <Text style={[
                    styles.statusText,
                    { color: item.isActive ? 'green' : 'red' }
                ]}>
                    {item.isActive ? 'AKTYWNY' : 'NIEAKTYWNY'}
                </Text>
            </View>

            <Text style={styles.infoText}>{item.firstName} {item.lastName}</Text>
            <Text style={styles.emailText}>{item.email}</Text>

            <View style={styles.actionsRow}>
                <TouchableOpacity
                    style={[styles.smallBtn, styles.btnBlue]}
                    onPress={() => navigation.navigate('UserDetails', { id: item.id })}
                >
                    <Text style={styles.btnTextSmall}>Szczegóły</Text>
                </TouchableOpacity>

                <TouchableOpacity
                    style={[styles.smallBtn, styles.btnOrange]}
                    onPress={() => navigation.navigate('UserEdit', { id: item.id })}
                >
                    <Text style={styles.btnTextSmall}>Edytuj</Text>
                </TouchableOpacity>

                <TouchableOpacity
                    style={[styles.smallBtn, item.isActive ? styles.btnRed : styles.btnGreen]}
                    onPress={() => toggleStatus(item.id!, item.isActive)}
                >
                    <Text style={styles.btnTextSmall}>
                        {item.isActive ? 'Dezaktywuj' : 'Aktywuj'}
                    </Text>
                </TouchableOpacity>
            </View>
        </View>
    );

    return (
        <View style={styles.container}>
            <View style={styles.searchContainer}>
                <TextInput
                    style={styles.searchInput}
                    placeholder="Wprowadź login"
                    value={search}
                    onChangeText={setSearch}
                />
                <TouchableOpacity style={styles.searchBtn} onPress={fetchUsers}>
                    <Text style={styles.btnText}>Szukaj</Text>
                </TouchableOpacity>
            </View>

            <TouchableOpacity
                style={styles.addBtn}
                onPress={() => navigation.navigate('UserNew')}
            >
                <Text style={styles.btnText}>+ Dodaj Klienta</Text>
            </TouchableOpacity>

            <FlatList
                data={users}
                keyExtractor={(item) => item.id?.toString() || Math.random().toString()}
                renderItem={renderUserItem}
                contentContainerStyle={styles.listContent}
            />
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#f0f2f5',
        padding: 10,
    },
    searchContainer: {
        flexDirection: 'row',
        marginBottom: 10,
    },
    searchInput: {
        flex: 1,
        backgroundColor: 'white',
        padding: 10,
        borderRadius: 5,
        borderWidth: 1,
        borderColor: '#ddd',
        marginRight: 10,
    },
    searchBtn: {
        backgroundColor: '#555',
        justifyContent: 'center',
        paddingHorizontal: 15,
        borderRadius: 5,
    },
    addBtn: {
        backgroundColor: '#007AFF',
        padding: 12,
        borderRadius: 5,
        alignItems: 'center',
        marginBottom: 15,
    },
    btnText: {
        color: 'white',
        fontWeight: 'bold',
    },
    listContent: {
        paddingBottom: 20,
    },
    card: {
        backgroundColor: 'white',
        padding: 15,
        marginBottom: 10,
        borderRadius: 8,
        shadowColor: "#000",
        shadowOffset: { width: 0, height: 1 },
        shadowOpacity: 0.2,
        shadowRadius: 1.41,
        elevation: 2,
    },
    cardHeader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginBottom: 5,
    },
    loginText: {
        fontSize: 18,
        fontWeight: 'bold',
    },
    statusText: {
        fontSize: 12,
        fontWeight: 'bold',
    },
    infoText: {
        fontSize: 16,
        color: '#333',
    },
    emailText: {
        fontSize: 14,
        color: '#666',
        marginBottom: 10,
    },
    actionsRow: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginTop: 10,
        borderTopWidth: 1,
        borderTopColor: '#eee',
        paddingTop: 10,
    },
    smallBtn: {
        paddingVertical: 6,
        paddingHorizontal: 10,
        borderRadius: 4,
    },
    btnTextSmall: {
        color: 'white',
        fontSize: 12,
        fontWeight: '600',
    },
    btnBlue: { backgroundColor: '#3498db' },
    btnOrange: { backgroundColor: '#f39c12' },
    btnRed: { backgroundColor: '#e74c3c' },
    btnGreen: { backgroundColor: '#2ecc71' },
});