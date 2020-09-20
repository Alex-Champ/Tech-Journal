high-availability {
    vrrp {
        group langroup0 {
            interface eth1
            priority 200
            virtual-address 10.0.5.1/24
            vrid 160
        }
        group optgroup0 {
            interface eth2
            priority 200
            virtual-address 10.0.6.1/24
            vrid 170
        }
        group wangroup0 {
            interface eth0
            priority 200
            virtual-address 10.0.17.70/24
            vrid 150
        }
    }
}
interfaces {
    ethernet eth0 {
        address 10.0.17.10/24
        description WAN
        hw-id 00:50:56:b3:d9:0f
    }
    ethernet eth1 {
        address 10.0.5.2/24
        description LAN
        hw-id 00:50:56:b3:ea:50
    }
    ethernet eth2 {
        address 10.0.6.2/24
        description OPT
        hw-id 00:50:56:b3:f6:bd
    }
    loopback lo {
    }
}
nat {
    destination {
        rule 10 {
            description "Proxy to Web Servers WAN-OPT"
            destination {
                port 80
            }
            inbound-interface eth0
            protocol tcp
            translation {
                address 10.0.6.10
            }
        }
        rule 20 {
            description "Port Forward: SSH to web01"
            destination {
                port 22
            }
            inbound-interface eth0
            protocol tcp
            translation {
                address 10.0.5.100
            }
        }
    }
    source {
        rule 100 {
            description "NAT for LAN-WAN"
            outbound-interface eth0
            source {
                address 10.0.5.0/24
            }
            translation {
                address masquerade
            }
        }
        rule 200 {
            description "NAT for OPT-LAN"
            outbound-interface eth0
            source {
                address 10.0.6.0/24
            }
            translation {
                address masquerade
            }
        }
    }
}
protocols {
    static {
        route 0.0.0.0/0 {
            next-hop 10.0.17.2 {
            }
        }
    }
}
service {
    dns {
        forwarding {
            allow-from 10.0.5.0/24
            allow-from 10.0.6.0/24
            listen-address 10.0.5.1
            listen-address 10.0.5.2
            listen-address 10.0.17.10
            listen-address 10.0.6.2
            listen-address 10.0.6.1
            name-server 10.0.17.2
        }
    }
    ssh {
        port 22
    }
}
system {
    config-management {
        commit-revisions 100
    }
    console {
    }
    host-name vyos
    login {
        user vyos {
            authentication {
                encrypted-password $6$Tf8fh/GcIL1$SZCjd50gLgpIHm3x.60qVFDKe9hhxXOtJxRVNOKmb7LFBbsvVqwTvF4nyl8HRPXfzsK6cng7xsNR4LlvL.NcY0
                plaintext-password ""
            }
        }
    }
    name-server 10.0.17.2
    ntp {
        server 0.pool.ntp.org {
        }
        server 1.pool.ntp.org {
        }
        server 2.pool.ntp.org {
        }
    }
    syslog {
        global {
            facility all {
                level info
            }
            facility protocols {
                level debug
            }
        }
    }
}


// Warning: Do not remove the following line.
// vyos-config-version: "broadcast-relay@1:cluster@1:config-management@1:conntrack@1:conntrack-sync@1:dhcp-relay@2:dhcp-server@5:dhcpv6-server@1:dns-forwarding@3:firewall@5:https@2:interfaces@11:ipoe-server@1:ipsec@5:l2tp@3:lldp@1:mdns@1:nat@5:ntp@1:pppoe-server@3:pptp@2:qos@1:quagga@6:salt@1:snmp@1:ssh@1:sstp@2:system@18:vrrp@2:vyos-accel-ppp@2:wanloadbalance@3:webgui@1:webproxy@2:zone-policy@1"
// Release version: 1.3-rolling-202007010117
