import React from 'react';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import Toast from 'react-native-toast-message';
import { Ionicons } from '@expo/vector-icons';
import UserList from '../src/pages/UserList';
import UserForm from '../src/pages/UserForm';
import UserDetails from '../src/pages/UserDetails';
import ReservationList from '../src/pages/ReservationList';
import ReservationForm from '../src/pages/ReservationForm';

const Stack = createNativeStackNavigator();
const Tab = createBottomTabNavigator();

function HomeTabs() {
    return (
        <Tab.Navigator screenOptions={({ route }) => ({
            tabBarIcon: ({ focused, color, size }) => {
                let iconName;

                if (route.name === 'UsersTab') {
                    iconName = focused ? 'people' : 'people-outline';
                } else if (route.name === 'ReservationsTab') {
                    iconName = focused ? 'calendar' : 'calendar-outline';
                }
                return <Ionicons name={iconName as any} size={size} color={color} />;
            },
            tabBarActiveTintColor: '#007AFF',
            tabBarInactiveTintColor: 'gray',
            headerShown: true,
        })}>
            <Tab.Screen name="UsersTab" component={UserList} options={{ title: 'Klienci' }} />
            <Tab.Screen name="ReservationsTab" component={ReservationList} options={{ title: 'Rezerwacje' }} />
        </Tab.Navigator>
    );
}
export default function App() {
    return (
        <>
            <Stack.Navigator>
                <Stack.Screen
                    name="Home"
                    component={HomeTabs}
                    options={{ headerShown: false }}
                />

                <Stack.Screen name="UserNew" component={UserForm} options={{ title: 'Dodaj Klienta' }} />
                <Stack.Screen name="UserEdit" component={UserForm} options={{ title: 'Edytuj Klienta' }} />
                <Stack.Screen name="UserDetails" component={UserDetails} options={{ title: 'Szczegóły Klienta' }} />
                <Stack.Screen name="ReservationNew" component={ReservationForm} options={{ title: 'Nowa Rezerwacja' }} />
            </Stack.Navigator>
            <Toast />
        </>
    );
}