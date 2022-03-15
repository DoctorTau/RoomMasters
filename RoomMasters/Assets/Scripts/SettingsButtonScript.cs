using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class SettingsButtonScript : MonoBehaviour
{
    bool isOpen = false;
    [SerializeField] GameObject panel;

    private void Awake()
    {
        panel.SetActive(false);
    }

    public void ShowHidePanel()
    {
        if (isOpen)
        {
            panel.SetActive(false);
            isOpen = false;
            return;
        }

        panel.SetActive(true);
        isOpen = true;
    }
}
